package com.playtomic.tests.wallet.model.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Builder
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel(description = "Wallet model")
public class WalletResponse {
    @NonNull
    @ApiModelProperty(notes = "Wallet identifier")
    Long id;

    @ApiModelProperty(notes = "Current wallet balance")
    String balance;
}
