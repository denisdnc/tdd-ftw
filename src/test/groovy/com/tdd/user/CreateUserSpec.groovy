package com.tdd.user

import br.com.six2six.fixturefactory.Fixture
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader
import com.tdd.user.domains.SerasaResponse
import com.tdd.user.domains.User
import com.tdd.user.gateways.database.UserGateway
import com.tdd.user.gateways.httpclient.SerasaGateway
import com.tdd.user.usecases.CreateUser
import spock.lang.Specification

class CreateUserSpec extends Specification{
    UserGateway repository = Mock(UserGateway)
    SerasaGateway serasaGateway = Mock(SerasaGateway)
    CreateUser createUser = new CreateUser(repository, serasaGateway)


    def setupSpec (){
        FixtureFactoryLoader.loadTemplates("com.tdd")
    }

    def "validar campos obrigatorios"(){
        given: "um usuario sem os dados obrigatorios"
            User user = new User()

        when: "criar um usuario"

            User result = createUser.execute(user)
        then: "retornar uma lista de erros"
            result.errors != null
            result.errors.size() == 2
            result.errors.any{error -> error.message == "CPF é obrigatório"}
            result.errors.any{error -> error.message == "Nome é obrigatório"}
        and: "não deve salvar esse usuário"
            0 * repository.save(_ as User)
    }

    def "criar usuario com sucesso" (){
        given: "um usuario valido"
            User user = Fixture.from(User.class).gimme("valid")

        when: "criar um usuario"
            User result = createUser.execute(user)


        then: "deve chamar a api do serasa passando o cpf informado"
        1 * serasaGateway.find(_ as String) >> {
            String document ->
                assert document == "123456789"

                Fixture.from(SerasaResponse.class).gimme("PENDING_DEBIT")
        }

        and: "salvar o usuario na base"
            1 * repository.save(_ as User) >> {
                User userExpected ->
                    assert userExpected.name == "Jack"
                    assert userExpected.document == "123456789"
            }


        and: "retornar o usuario salvo"
            result != null
            result.name == "Jack"
            result.document == "123456789"
            result.errors.size() == 0

    }

    def "deve consultar a api do serasa e retornar o status"(){
        given: "um usuario valido"
        User user = Fixture.from(User.class).gimme("valid")

        when: "criar um usuario"
        User result = createUser.execute(user)

        then: "deve chamar a api do serasa passando o cpf informado"
        1 * serasaGateway.find(_ as String) >> {
            String document ->
                assert document == "123456789"

                Fixture.from(SerasaResponse.class).gimme("NO_DEBIT")
        }

        and : "retornar o status esperado"
        result.status == "NO_DEBIT"
    }





}
