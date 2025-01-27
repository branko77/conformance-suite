package net.openid.conformance.sequence.client;

import net.openid.conformance.condition.client.AddClientAssertionToTokenEndpointRequest;
import net.openid.conformance.condition.client.CreateClientAuthenticationAssertionClaimsWithIssAudience;
import net.openid.conformance.condition.client.SignClientAuthenticationAssertion;
import net.openid.conformance.sequence.AbstractConditionSequence;

public class CreateJWTClientAuthenticationAssertionWithIssAudAndAddToTokenEndpointRequest extends AbstractConditionSequence {

	@Override
	public void evaluate() {
		callAndStopOnFailure(CreateClientAuthenticationAssertionClaimsWithIssAudience.class);
		callAndStopOnFailure(SignClientAuthenticationAssertion.class);
		callAndStopOnFailure(AddClientAssertionToTokenEndpointRequest.class);
	}

}
