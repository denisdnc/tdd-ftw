import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.tdd.user.domains.SerasaStatusWrapper
import com.tdd.user.domains.User
import com.tdd.user.gateways.database.UserDatabaseGateway
import com.tdd.user.gateways.httpclient.SerasaGateway
import com.tdd.user.gateways.httpclient.SerasaIntegrationStatus
import com.tdd.user.usecases.GetSerasaStatus
import spock.lang.Specification


class GetSerasaStatusSpec extends Specification {

    /** Dependencies */
    UserDatabaseGateway userDatabaseGateway = Mock(UserDatabaseGateway)
    SerasaGateway serasaGateway = Mock(SerasaGateway)

    /** Use case to test */
    GetSerasaStatus getSerasaStatus = new GetSerasaStatus(userDatabaseGateway, serasaGateway)

    def setupSpec() {
        FixtureFactoryLoader.loadTemplates("com.tdd")
    }

    def "validate mandatory attribute"() {
        given: "an empty document"

        String document = ""

        when: "get serasa status"

        SerasaStatusWrapper result = getSerasaStatus.execute(document)

        then: "should return error"

        result.errors.size() == 1
        result.errors.stream().anyMatch { error -> error.message == "document is mandatory" }
    }

    def "check user exists"() {
        given: "an document not present in the system"

        String document = "73485234479"

        when: "get serasa status"

        SerasaStatusWrapper result = getSerasaStatus.execute(document)

        then: "should return error"

        result.document == "73485234479"
        result.status == null
        result.errors.size() == 1
        result.errors.stream().anyMatch { error -> error.message == "document not found" }
    }

    def "validate code pending debit"() {
        given: "an document present in the system"

        String document = "123456789"
        userDatabaseGateway.findByDocument(document) >> Fixture.from(User.class).gimme("valid")

        when: "get serasa status"

        SerasaStatusWrapper result = getSerasaStatus.execute(document)

        then: "the serasa API should return status 1"

        serasaGateway.getStatus(document) >> Fixture.from(SerasaIntegrationStatus.class).gimme("pending debit")

        and: "returned status should be pending"

        result.status.toString() == "PENDING_DEBIT"
    }

    def "validate code no debit"() {
        given: "an document present in the system"

        String document = "123456789"
        userDatabaseGateway.findByDocument(document) >> Fixture.from(User.class).gimme("valid")

        when: "get serasa status"

        SerasaStatusWrapper result = getSerasaStatus.execute(document)

        then: "the serasa API should return status 2"

        serasaGateway.getStatus(document) >> Fixture.from(SerasaIntegrationStatus.class).gimme("no debit")

        and: "returned status should be pending"

        result.status.toString() == "NO_DEBIT"
    }

}