package net.openid.conformance.condition.as;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class OIDCCGenerateServerConfigurationIdTokenSigningAlgES256Only extends OIDCCGenerateServerConfiguration {

	@Override
	protected void addIdTokenSigningAlgValuesSupported(JsonObject server) {
		JsonArray values = new JsonArray();
		values.add("ES256");
		server.add("id_token_signing_alg_values_supported", values);
	}
}
