package net.openid.conformance.condition.rs;

import com.google.common.base.Strings;
import com.google.gson.JsonObject;
import net.openid.conformance.condition.AbstractCondition;
import net.openid.conformance.condition.PostEnvironment;
import net.openid.conformance.condition.PreEnvironment;
import net.openid.conformance.testmodule.Environment;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FAPIBrazilGenerateGetPaymentConsentResponse extends AbstractCondition {

	@Override
	@PreEnvironment(required = {"consent_response"}, strings = {"fapi_interaction_id", "requested_consent_id"})
	@PostEnvironment(required = {"get_consent_response", "get_consent_response_headers"})
	public Environment evaluate(Environment env) {
		String requestedConsentId = env.getString("requested_consent_id");
		String existingConsentId = env.getString("consent_response", "data.consentId");
		if(!requestedConsentId.equals(existingConsentId)) {
			throw error("Requested consent id does not match the consent id created by this test",
				args("requested_consent_id", requestedConsentId, "expected_consent_id", existingConsentId));
		}

		JsonObject existingConsent = env.getObject("consent_response");

		JsonObject consentResponse = new JsonObject();
		consentResponse.add("data", existingConsent.get("data"));
		if(existingConsent.has("links")) {
			consentResponse.add("links", existingConsent.get("links"));
		}
		consentResponse.add("aud", existingConsent.get("aud"));
		consentResponse.add("iss", existingConsent.get("iss"));
		consentResponse.addProperty("iat", Instant.now().getEpochSecond());
		consentResponse.addProperty("jti", UUID.randomUUID().toString());

		JsonObject meta = new JsonObject();
		meta.addProperty("totalRecords", 1);
		meta.addProperty("totalPages", 1);
		Instant baseDateRough = Instant.now();
		Instant baseDate = baseDateRough.minusNanos(baseDateRough.getNano());
		String requestDateTime = DateTimeFormatter.ISO_INSTANT.format(baseDate);
		meta.addProperty("requestDateTime", requestDateTime);
		consentResponse.add("meta", meta);

		env.putObject("get_consent_response", consentResponse);

		String fapiInteractionId = env.getString("fapi_interaction_id");
		if (Strings.isNullOrEmpty(fapiInteractionId)) {
			throw error("Couldn't find FAPI Interaction ID");
		}

		JsonObject headers = new JsonObject();
		headers.addProperty("x-fapi-interaction-id", fapiInteractionId);

		env.putObject("get_consent_response_headers", headers);

		logSuccess("Created consent response", args("response", consentResponse));

		return env;

	}

}
