import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.buildSteps.MavenBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.ScriptBuildStep
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.projectFeatures.githubIssues
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

	mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.04"

project {

	vcsRoot(VCS)

	buildType(BuildTestPublishDeployContainerImage)
	buildType(BuildTestPRs)

	params {
		param("env.CLOUD_K8S_CLUSTER_NAME", "%vault:dts-common/data/AZURE_DTS_DEV_SUB!/CLOUD_K8S_CLUSTER_NAME%")
		param("env.CLOUD_K8S_RG", "%vault:dts-common/data/AZURE_DTS_DEV_SUB!/CLOUD_K8S_RG%")
		param("env.CLOUD_SUBSCRIPTION", "%vault:dts-common/data/AZURE_DTS_DEV_SUB!/CLOUD_SUBSCRIPTION%")
		param("env.CLOUD_TENANT_ID", "%vault:dts-common/data/AZURE_DTS_DEV_SUB!/CLOUD_TENANT_ID%")
	}

	features {
		githubIssues {
			id = "PROJECT_EXT_16"
			displayName = "DTS-STN/passport-status-api"
			repositoryURL = "https://github.com/DTS-STN/passport-status-api"
		}
	}

	cleanup {
		keepRule {
			id = "KEEP_RULE_1"
			keepAtLeast = builds(3)
			applyToBuilds {
				withStatus = successful()
			}
			dataToKeep = everything()
			applyPerEachBranch = true
			preserveArtifactsDependencies = true
		}
	}
}

object BuildTestPRs : BuildType({
	name = "Build and test pull requests"

	artifactRules = """
		target/dependency-check-report.html
		target/**.jar
	""".trimIndent()
	publishArtifacts = PublishMode.SUCCESSFUL

	vcs {
		root(VCS)
	}

	steps {
		script {
			name = "az acr login"
			scriptContent = """
				set -e

				# ACR login so future steps will work
				az login --service-principal --username %env.TEAMCITY_SPN_ID% --password %env.TEAMCITY_SPN_PASS% --tenant %env.CLOUD_TENANT_ID%
				az acr login --name %env.CLOUD_ACR_DOMAIN% --subscription %env.CLOUD_SUBSCRIPTION%
			""".trimIndent()
		}
		maven {
			name = "Build and verify Maven artifact"
			goals = "clean verify"
			dockerImagePlatform = MavenBuildStep.ImagePlatform.Linux
			dockerPull = true
			dockerImage = "%env.CLOUD_ACR_DOMAIN%/passport-status-api-builder:latest"
			dockerRunParameters = "--volume /var/run/docker.sock:/var/run/docker.sock"
			coverageEngine = idea {
				includeClasses = "ca.gov.dtsstn.passport.api.*"
			}
		}
	}

	triggers {
		vcs {
			branchFilter = ""
		}
	}

	failureConditions {
		executionTimeoutMin = 10
	}

	features {
		pullRequests {
			vcsRootExtId = "${VCS.id}"
			provider = github {
				authType = token {
					token = "zxx310f6e7bdd0a91d00c95c7d2b4256d135cac61b1d28b40f0378b02b2a347797887ce4d9bcd94ba98775d03cbe80d301b"
				}
				filterTargetBranch = """
					+:refs/heads/main
					+:refs/heads/release/*
				""".trimIndent()
				filterAuthorRole = PullRequests.GitHubRoleFilter.EVERYBODY
			}
		}
		commitStatusPublisher {
			publisher = github {
				githubUrl = "https://api.github.com"
				authType = personalToken {
					token = "zxx310f6e7bdd0a91d00c95c7d2b4256d135cac61b1d28b40f0378b02b2a347797887ce4d9bcd94ba98775d03cbe80d301b"
				}
			}
		}
	}
})

object BuildTestPublishDeployContainerImage : BuildType({
	name = "Build, test, publish, and deploy main branch"

	artifactRules = """
		target/dependency-check-report.html
		target/**.jar
	""".trimIndent()

	params {
		param("env.BUILD_REV", "00000000")
		param("env.BUILD_ID", "00000")
		param("env.BUILD_DATE", "0000-00-00")
	}

	vcs {
		root(VCS)
	}

	steps {
		script {
			name = "Dump environment variables"
			scriptContent = """
				set -e
				env | sort
			""".trimIndent()
		}
		script {
			name = "Set build parameters"
			scriptContent = """
				set -e

				echo "##teamcity[setParameter name='env.BUILD_DATE' value='${'$'}(date -u +%%Y%%m%%d)']"
				echo "##teamcity[setParameter name='env.BUILD_ID' value='${'$'}(printf %05d %env.BUILD_NUMBER%)']"
				echo "##teamcity[setParameter name='env.BUILD_REV' value='${'$'}(git rev-parse --short=8 HEAD)']"
				echo "##teamcity[setParameter name='env.BUILD_TIMESTAMP' value='${'$'}(date -u +%%Y-%%m-%%dT%%H:%%M:%%SZ)']"
			""".trimIndent()
		}
		script {
			name = "az acr login"
			scriptContent = """
				set -e

				# ACR login so future steps will work
				az login --service-principal --username %env.TEAMCITY_SPN_ID% --password %env.TEAMCITY_SPN_PASS% --tenant %env.CLOUD_TENANT_ID%
				az acr login --name %env.CLOUD_ACR_DOMAIN% --subscription %env.CLOUD_SUBSCRIPTION%
			""".trimIndent()
		}
		maven {
			name = "mvn clean verify"
			goals = "clean verify"
			runnerArgs = """
				--define image.name=%env.CLOUD_ACR_DOMAIN%/passport-status-api
				--define revision=%env.BUILD_DATE%-%env.BUILD_ID%-%env.BUILD_REV%
			""".trimIndent()
			dockerImagePlatform = MavenBuildStep.ImagePlatform.Linux
			dockerPull = true
			dockerImage = "%env.CLOUD_ACR_DOMAIN%/passport-status-api-builder:latest"
			dockerRunParameters = "--volume /var/run/docker.sock:/var/run/docker.sock"
			coverageEngine = idea {
				includeClasses = "ca.gov.dtsstn.passport.api.*"
			}
		}
		script {
			name = "mvn clean spring-boot:build-image && docker push"
			scriptContent = """
				set -e

				IMAGE_NAME=%env.CLOUD_ACR_DOMAIN%/passport-status-api
				IMAGE_REVISION=%env.BUILD_DATE%-%env.BUILD_ID%-%env.BUILD_REV%

				# build the container image using Spring Boot's Maven plugin via buildpack
				# (skipping tests because they were performed in previous build step)
				mvn --batch-mode spring-boot:build-image \
					--define image.name=${'$'}IMAGE_NAME \
					--define maven.test.skip=true \
					--define revision=${'$'}IMAGE_REVISION

				# ACR login so future steps will work
				az login --service-principal \
					--username %env.TEAMCITY_SPN_ID% \
					--password %env.TEAMCITY_SPN_PASS% \
					--tenant %env.CLOUD_TENANT_ID%

				az acr login \
					--name %env.CLOUD_ACR_DOMAIN% \
					--subscription %env.CLOUD_SUBSCRIPTION%

				# Main branch is always tagged as 'latest'
				docker push ${'$'}IMAGE_NAME

				# Tag and push full version (ex: 0.0.0) and git sha
				docker tag ${'$'}IMAGE_NAME ${'$'}IMAGE_NAME:${'$'}IMAGE_REVISION
				docker push ${'$'}IMAGE_NAME:${'$'}IMAGE_REVISION
			""".trimIndent()
			formatStderrAsError = true
			dockerImagePlatform = ScriptBuildStep.ImagePlatform.Linux
			dockerPull = true
			dockerImage = "%env.CLOUD_ACR_DOMAIN%/passport-status-api-builder:latest"
			dockerRunParameters = "--volume /var/run/docker.sock:/var/run/docker.sock"
		}
		script {
			name = "kubectl rollout restart deployment/passport-status-api-dev"
			scriptContent = """
				set -e

				az login --service-principal \
					--username %env.AAD_SERVICE_PRINCIPAL_CLIENT_ID% \
					--password %env.AAD_SERVICE_PRINCIPAL_CLIENT_SECRET% \
					--tenant %env.CLOUD_TENANT_ID%

				az aks get-credentials --overwrite-existing \
					--resource-group %env.CLOUD_K8S_RG% \
					--name %env.CLOUD_K8S_CLUSTER_NAME% \
					--subscription %env.CLOUD_SUBSCRIPTION%

				kubelogin convert-kubeconfig --login spn

				kubectl rollout restart deployment/passport-status-api-dev --namespace %env.PROJECT%
			""".trimIndent()
		}
	}

	triggers {
		vcs {
			branchFilter = ""
			enableQueueOptimization = false
		}
	}

	failureConditions {
		executionTimeoutMin = 10
	}
})

object VCS : GitVcsRoot({
	name = "Passport Status API"
	url = "https://github.com/DTS-STN/passport-status-api.git"
	branch = "refs/heads/main"
	branchSpec = "+:refs/heads/release/*"
})
