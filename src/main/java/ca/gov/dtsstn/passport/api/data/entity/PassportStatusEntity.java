package ca.gov.dtsstn.passport.api.data.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.immutables.builder.Builder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SuppressWarnings({ "serial" })
@Entity(name = "PassportStatus")
public class PassportStatusEntity extends AbstractEntity {

	@Column(length = 256, nullable = false)
	private String applicationRegisterSid;

	@Column(nullable = false)
	private LocalDate dateOfBirth;

	@Column(length = 256, nullable = false)
	private String email;

	@Column(length = 32, nullable = false)
	private String fileNumber;

	@Column(length = 64, nullable = false)
	private String givenName;

	@Column(length = 32, nullable = true)
	private String manifestNumber;

	@Column(length = 64, nullable = false)
	private String surname;

	@ManyToOne
	private SourceCodeEntity sourceCode;

	@ManyToOne
	private StatusCodeEntity statusCode;

  @ManyToOne
  private DeliveryMethodCodeEntity deliveryMethodCode;

  @ManyToOne
  private ServiceLevelCodeEntity serviceLevelCode;

  @Column(nullable = false)
  private LocalDate appReceivedDate;

  @Column(nullable = true)
  private LocalDate appReviewedDate;

  @Column(nullable = true)
  private LocalDate appPrintedDate;

  @Column(nullable = true)
  private LocalDate appCompletedDate;

	@Column(nullable = false)
	private LocalDate statusDate;

	@Column(nullable = false)
	private Long version;

	public PassportStatusEntity() {
		super();
	}

	@Builder.Constructor
	protected PassportStatusEntity( // NOSONAR (too many parameters)
    @Nullable String id,
    @Nullable String createdBy,
    @Nullable Instant createdDate,
    @Nullable String lastModifiedBy,
    @Nullable Instant lastModifiedDate,
    @Nullable Boolean isNew,
    @Nullable String applicationRegisterSid,
    @Nullable LocalDate dateOfBirth,
    @Nullable String email,
    @Nullable String fileNumber,
    @Nullable String givenName,
    @Nullable String manifestNumber,
    @Nullable String surname,
    @Nullable SourceCodeEntity sourceCode,
    @Nullable StatusCodeEntity statusCode,
    @Nullable DeliveryMethodCodeEntity deliveryMethodCode,
    @Nullable ServiceLevelCodeEntity serviceLevelCode,
    @Nullable LocalDate appReceivedDate,
    @Nullable LocalDate appReviewedDate,
    @Nullable LocalDate appPrintedDate,
    @Nullable LocalDate appCompletedDate,
    @Nullable LocalDate statusDate,
    @Nullable Long version
  ) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, isNew);
		this.applicationRegisterSid = applicationRegisterSid;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.fileNumber = fileNumber;
		this.givenName = givenName;
		this.manifestNumber = manifestNumber;
		this.surname = surname;
		this.sourceCode = sourceCode;
		this.statusCode = statusCode;
    this.deliveryMethodCode = deliveryMethodCode;
    this.serviceLevelCode = serviceLevelCode;
    this.appReceivedDate = appReceivedDate;
    this.appReviewedDate = appReviewedDate;
    this.appPrintedDate = appPrintedDate;
    this.appCompletedDate = appCompletedDate;
		this.statusDate = statusDate;
		this.version = version;
	}

	public String getApplicationRegisterSid() {
		return applicationRegisterSid;
	}

	public void setApplicationRegisterSid(String applicationRegisterSid) {
		this.applicationRegisterSid = applicationRegisterSid;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getManifestNumber() {
		return manifestNumber;
	}

	public void setManifestNumber(String manifestNumber) {
		this.manifestNumber = manifestNumber;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public SourceCodeEntity getSourceCode() {
		return this.sourceCode;
	}

	public void setSourceCode(SourceCodeEntity sourceCode) {
		this.sourceCode = sourceCode;
	}

	public StatusCodeEntity getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(StatusCodeEntity statusCode) {
		this.statusCode = statusCode;
	}

  public DeliveryMethodCodeEntity getDeliveryMethodCode() {
    return deliveryMethodCode;
  }

  public void setDeliveryMethodCode(DeliveryMethodCodeEntity deliveryMethodCode) {
    this.deliveryMethodCode = deliveryMethodCode;
  }

  public ServiceLevelCodeEntity getServiceLevelCode() {
		return serviceLevelCode;
	}

	public void setServiceLevelCode(ServiceLevelCodeEntity serviceLevelCode) {
		this.serviceLevelCode = serviceLevelCode;
	}

  public LocalDate getAppReceivedDate() {
		return appReceivedDate;
	}

	public void setAppReceivedDate(LocalDate appReceivedDate) {
		this.appReceivedDate = appReceivedDate;
	}

	public LocalDate getAppReviewedDate() {
		return appReviewedDate;
	}

	public void setAppReviewedDate(LocalDate appReviewedDate) {
		this.appReviewedDate = appReviewedDate;
	}

	public LocalDate getAppPrintedDate() {
		return appPrintedDate;
	}

	public void setAppPrintedDate(LocalDate appPrintedDate) {
		this.appPrintedDate = appPrintedDate;
	}

	public LocalDate getAppCompletedDate() {
		return appCompletedDate;
	}

	public void setAppCompletedDate(LocalDate appCompletedDate) {
		this.appCompletedDate = appCompletedDate;
	}
	public LocalDate getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(LocalDate statusDate) {
		this.statusDate = statusDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public boolean equals(Object obj) {
		// keeps SonarLint happy
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// keeps SonarLint happy
		return super.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
    .append("super", super.toString())
    .append("applicationRegisterSid", applicationRegisterSid)
    .append("dateOfBirth", dateOfBirth)
    .append("email", email)
    .append("fileNumber", fileNumber)
    .append("givenName", givenName)
    .append("manifestNumber", manifestNumber)
    .append("surname", surname)
    .append("sourceCode", Optional.ofNullable(sourceCode)
      .map(SourceCodeEntity::getCode)
      .orElse(null))
    .append("statusCode", Optional.ofNullable(statusCode)
      .map(StatusCodeEntity::getCode)
      .orElse(null))
    .append("deliveryMethodCode", Optional.ofNullable(deliveryMethodCode)
      .map(DeliveryMethodCodeEntity::getCode)
      .orElse(null))
    .append("serviceLevelCode", Optional.ofNullable(serviceLevelCode)
      .map(ServiceLevelCodeEntity::getCode)
      .orElse(null))
    .append("appReceivedDate", appReceivedDate)
    .append("appReviewedDate", appReviewedDate)
    .append("appPrintedDate", appPrintedDate)
    .append("appCompletedDate", appCompletedDate)
    .append("statusDate", statusDate)
    .append("version", version)
    .toString();
	}
}
