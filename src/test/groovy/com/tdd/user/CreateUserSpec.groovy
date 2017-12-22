package com.tdd.user

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.tdd.user.domains.SerasaWrapper
import com.tdd.user.domains.User
import com.tdd.user.gateways.database.UserGateway
import com.tdd.user.gateways.httpclient.SerasaGateway
import com.tdd.user.usecases.CreateUser
import spock.lang.Specification

class CreateUserSpec extends Specification{
    SerasaGateway serasaGateway = Mock(SerasaGateway)
    UserGateway userGateway= Mock(UserGateway)
    CreateUser createUser = new CreateUser(userGateway, serasaGateway)



    def setupSpec (){
        FixtureFactoryLoader.loadTemplates("com.tdd")
    }

    def "Deve validar campos mandatórios" (){
        given: "receber as informações de usuário inválidos"
        User user = new User()

        when: "tentar criar o usuário"
        User result = createUser.execute(user)

        then: "retornar erros de nome e documento"
        result.errors != null
        result.errors.size() == 2
        result.errors.stream().anyMatch{error -> error.message == "name is mandatory"}
        result.errors.stream().anyMatch{error -> error.message == "document is mandatory"}
    }

    def "Deve retornar os dados do usuario sem erros"() {
        given: "receber as informações de usuário válido"
        User user = Fixture.from(User.class).gimme("valid")

        and: "tenha um status valido no serasa"
        serasaGateway.find(_) >> Fixture.from(SerasaWrapper.class).gimme("pendente")

        when: "tentar criar um usuário"
        User result = createUser.execute(user);

        then: "um usuario válido deve ser retornado sem erros"
        result.errors.isEmpty()
        result.name == "Jack"
        result.document == "123456789"
    }

    def "Deve salvar usuario no banco"() {
        given: "receber as informações de usuário válido"
        User user = Fixture.from(User.class).gimme("valid")

        and: "tenha um status valido no serasa"

        serasaGateway.find(_) >> Fixture.from(SerasaWrapper.class).gimme("pendente")

        when: "tentar criar um usuário"
        createUser.execute(user)

        then: "Chamar o banco de dados passando os atributos"

        1 * userGateway.save(_ as User ) >> { User item ->
            assert item.name == "Jack"
            assert item.document == "123456789"
        }
    }

    def "Em caso de erro não deve salvar no banco"() {
        given: "receber as informações de usuário inválidos"
        User user = new User()

        when: "tentar criar o usuário"
        createUser.execute(user)

        then: "Não deve salvar no banco"
        0 * userGateway.save(_ as User )
    }

    def "validate user serasa status"(){
        given: "an valid user"
        User user = Fixture.from(User.class).gimme("valid")

        and: "tenha um status valido no serasa"

        serasaGateway.find(_) >> Fixture.from(SerasaWrapper.class).gimme("pendente")

        when: "tentar criar um usuário"
        User item = createUser.execute(user)

        then: "Deve retornar o status do serasa"
        item.status != null
    }

}
