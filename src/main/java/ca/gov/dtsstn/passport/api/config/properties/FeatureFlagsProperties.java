package ca.gov.dtsstn.passport.api.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Ken Blanchard (ken.blanchard@hrsdc-rhdcc.gc.ca)
 */
@ConfigurationProperties("application.feature-flags")
public class FeatureFlagsProperties {
	private List<String> hiddenManifests = List.of();

  public List<String> getHiddenManifests() {
      return hiddenManifests;
  }

  public void setHiddenManifests(List<String> list) {
      this.hiddenManifests = list;
  }
}
