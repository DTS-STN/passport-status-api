package ca.gov.dtsstn.passport.api.config.properties;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for hiding the manifest numbers from client/public view.
 * 
 * Mapping of status code to boolean value.
 *
 * @author Ken Blanchard (ken.blanchard@hrsdc-rhdcc.gc.ca)
 */
@ConfigurationProperties("application.feature-flags.hide-manifest")
public class HideManifestProperties {
  private Map<String, Boolean> map;

  public Map<String, Boolean> getMap() {
      return map;
  }

  public void setMap(Map<String, Boolean> map) {
      this.map = map;
  }
}