package com.npst.accounts.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Accounts extends BaseEntity {

    private Integer customerId;

    @Id
    private Long accountNumber;
    private String accountType;
    private String branchAddress;

}
