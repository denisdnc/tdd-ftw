# Exercício TDD

## Feature: Criação de usuário e consulta de status no SERASA

Como: usuário do sistema  
Eu quero: me cadastrar no sistema  
Para que: possa saber se estou com dívidas  

Devemos:
- receber o CPF informado pelo usuário  
- o nome  
- retornar aos seguintes atributos:  

URL a ser criado: GET: /users  
```json
  {
    "name": "Jack",
    "document": "04715476975",
    "status": "NO_DEBIT",
    "errors": [{
        "message": "document is mandatory"
    },]
  }
```
Regras:
- Nome é obrigatório e não pode ser vazio
- CPF é obrigatório e não pode ser vazio
- Consultar API do SERASA e retornar o status

Status para retonar:
- 201 em caso de sucesso
- 422 em caso de dos campos obrigatórios não existirem

### Descrição da API do SERASA

url: http://tddftw.getsandbox.com/users/cpf/status/{cpf}

possíveis retornos da mock:

Entrada: 04715476975

Saída:
status: 200
```json
{
    "codigoDeStatus": "NO_DEBIT" 
}
```

Entrada: 73485234478

Saída:
status: 200
```json
{
    "codigoDeStatus": "PENDING_DEBIT"
}
```

Entrada: demais valores

Saída:
status: 422
```json
{
    "errors": [{
        "message": "cpf nao cadastrado"
    }]
}
```

