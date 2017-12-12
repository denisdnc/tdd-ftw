package com.tdd.user

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.tdd.user.domains.User
import com.tdd.user.gateways.database.UserDatabaseGateway
import com.tdd.user.usecases.CreateUser
import spock.lang.Specification


class CreateUserSpec extends Specification {

    UserDatabaseGateway userDatabaseGateway = Mock(UserDatabaseGateway)
    CreateUser createUser = new CreateUser(userDatabaseGateway)

    def setupSpec() {
        FixtureFactoryLoader.loadTemplates("com.tdd")
    }

    def "validate mandatory parameters"() {
        given: "an user without mandatory parameters"

        User user = new User(null, "", "", null)

        when: "create user"

        User result = createUser.execute(user)

        then: "should returns errors"

        result.errors.stream().anyMatch { error -> error.message == "name is mandatory" }
        result.errors.stream().anyMatch { error -> error.message == "document is mandatory" }
    }

    def "create user with success"() {
        given: "an valid user"

        User user = Fixture.from(User.class).gimme("valid")

        when: "create user"

        User result = createUser.execute(user)

        then: "user gateway should return user from database"

        userDatabaseGateway.create(_ as User) >> user

        and: "should be create user"

        result.name == "Jack"
        result.document == "123456789"
    }

}