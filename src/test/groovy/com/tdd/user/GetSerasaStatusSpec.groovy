import com.tdd.user.domains.SerasaStatus
import com.tdd.user.gateways.database.UserDatabaseGateway
import com.tdd.user.usecases.GetSerasaStatus
import spock.lang.Specification


class GetSerasaStatusSpec extends Specification {

    /** Dependencies */
    UserDatabaseGateway userDatabaseGateway = Mock(UserDatabaseGateway)

    /** Use case to test */
    GetSerasaStatus getSerasaStatus = new GetSerasaStatus(userDatabaseGateway)

    def "validate mandatory attribute"() {
        given: "an empty document"

        String document = ""

        when: "get serasa status"

        SerasaStatus result = getSerasaStatus.execute(document)

        then: "should return error"

        result.errors.size() == 1
        result.errors.stream().anyMatch { error -> error.message == "document is mandatory" }
    }

    def "check user exists"() {
        given: "an document not present in the system"

        String document = "73485234479"

        when: "get serasa status"

        SerasaStatus result = getSerasaStatus.execute(document)

        then: "should return error"

        result.document == "73485234479"
        result.status == null
        result.errors.size() == 1
        result.errors.stream().anyMatch { error -> error.message == "document not found" }
    }

}