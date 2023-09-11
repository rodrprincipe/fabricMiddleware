# Fabrick Middleware

Fabrick Middleware provides a connection layer to endpoints for bank account operations such as:
- Balance
- Transactions
- MoneyTransfer

## Installation

To run the project locally we can install the [Maven Wrapper plugin](https://maven.apache.org/wrapper/maven-wrapper-plugin/)

The wrapper should work with different operating systems such as:

After that, we can run our goals like this for the Unix system:

```bash
./mvnw clean install
```

And the following command for Windows:

```bash
mvnw.cmd clean install
```

If we don’t have the specified Maven in the wrapper properties, it’ll be downloaded and installed in the folder $USER_HOME/.m2/wrapper/dists of the system.

Now we can run our Spring-Boot project:

```bash
./mvnw spring-boot:run
```

## Usage

```bash
# Balance
curl --request GET 'http://localhost:8081/bankAccount/{accountId}/balance'

# Transactions
curl --request GET 'http://localhost:8081/bankAccount/{accountId}/transactions?fromAccountingDate={YYYY-MM-DD}&toAccountingDate={YYYY-MM-DD}'

# Money Transfer
curl --request POST 'http://localhost:8081/bankAccount/{accountId}/moneyTransfer'
     --data '{payload}'
```
Replace parameters with proper values of:
- `{accountId}`
  - 14537780
- `{YYYY-MM-DD}` 
  - 2019-01-01
  - 2019-12-31
- `{payload}`

### Example of payload for money transfer
```json
{
  "creditor": {
    "name": "John Doe",
    "account": {
      "accountCode": "IT23A0336844430152923804660",
      "bicCode": "SELBIT2BXXX"
    },
    "address": {
      "address": null,
      "city": null,
      "countryCode": null
    }
  },
  "executionDate": "2019-04-01",
  "uri": "REMITTANCE_INFORMATION",
  "description": "Payment invoice 75/2017",
  "amount": 800,
  "currency": "EUR",
  "isUrgent": false,
  "isInstant": false,
  "feeType": "SHA",
  "feeAccountId": "45685475",
  "taxRelief": {
    "taxReliefId": "L449",
    "isCondoUpgrade": false,
    "creditorFiscalCode": "56258745832",
    "beneficiaryType": "NATURAL_PERSON",
    "naturalPersonBeneficiary": {
      "fiscalCode1": "MRLFNC81L04A859L",
      "fiscalCode2": null,
      "fiscalCode3": null,
      "fiscalCode4": null,
      "fiscalCode5": null
    },
    "legalPersonBeneficiary": {
      "fiscalCode": null,
      "legalRepresentativeFiscalCode": null
    }
  }
}
```

## License

[MIT](https://choosealicense.com/licenses/mit/)