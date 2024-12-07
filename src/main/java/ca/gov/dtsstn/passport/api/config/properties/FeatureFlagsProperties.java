package ca.gov.dtsstn.passport.api.config.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ken Blanchard (ken.blanchard@hrsdc-rhdcc.gc.ca)
 */
@ConfigurationProperties("application.feature-flags")
public class FeatureFlagsProperties {
	private Map<String, Boolean> hideManifest = Map.of();

  public Map<String, Boolean> getHideManifest() {
      return hideManifest;
  }

  public void setHideManifest(Map<String, Boolean> map) {
      this.hideManifest = map;
  }
}
