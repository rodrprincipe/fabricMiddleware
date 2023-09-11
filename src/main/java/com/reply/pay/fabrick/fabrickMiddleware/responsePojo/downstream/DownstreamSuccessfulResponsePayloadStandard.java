package com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream;

import com.reply.pay.fabrick.fabrickMiddleware.responsePojo.downstream.payload.PayloadStandard;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonIgnoreProperties(ignoreUnknown = true)
public class DownstreamSuccessfulResponsePayloadStandard extends DownstreamSuccessfulResponse {
    private PayloadStandard payload;

}