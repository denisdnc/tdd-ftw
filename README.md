# Exercício TDD

## Feature: Criação de usuário

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
    "status": "0",
    "errors": [{
        "message": "document is mandatory"
    },]
  }
```
http://tddftw.getsandbox.com/users/cpf/status/73485234478

Regras:
- Nome é obrigatório e não pode ser vazio
- CPF é obrigatório e não pode ser vazio

Status para retonar:
- 201 em caso de sucesso
- 422 em caso de dos campos obrigatórios não existirem
- 500 em caso de erro de servidor


## Feature: Validação de CPF no SERASA

Como: usuário do sistema
Eu quero: consultar meu CPF no SERASA
Para que: possa saber se estou com dívidas

Devemos:
- receber o CPF informado pelo usuário
- consultar API do SERASA
- retornar aos seguintes atributos:

URL a ser criado: GET: /users/serasa/status/{document}
```json
  {
    "document": "04715476975",
    "status": "PENDING_DEBIT",
    "errors": [{
        "message": "document not found"
    }]
  }
```

Regras:
- validar campo obrigatório: CPF
- verificar se ele existe no sistema e caso não exista devolver erro
- traduzir códigos numéricos da API do SERASA para os códigos abaixo:
    - Possíveis status:
        - 1 = PENDING_DEBIT
        - 2 = NO_DEBIT

Status para retonar:
- 200 em caso de sucesso
- 422 em caso de CPF não cadastrado
- 500 em caso de erro de servidor
- 422 em CPF não encontrado na API do SERASA - opcional
    
detalhes da API do SERASA:
url: http://wispy-thunder-8951.getsandbox.com/users/cpf/status/{cpf}

possíveis retornos da mock:

Entrada: 04715476975

Saída:
status: 200
```json
{
    "status": "NO_DEBIT" 
}
```

Entrada: 73485234478

Saída:
status: 200
```json
{
    "status": "PENDING_DEBIT"
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

