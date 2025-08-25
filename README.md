# Avaliação Técnica - Backend Java | Magnum BK

## 📘 Sobre o Projeto  
Este projeto foi desenvolvido como parte de uma avaliação técnica com foco em integração de APIs, processamento assíncrono com filas (Kafka) e gerenciamento de dados em um ambiente distribuído.  

Ele é composto por duas APIs que interagem para consumir dados da tabela FIPE, processá-los e armazená-los em um banco de dados relacional.  

O sistema foi arquitetado para demonstrar o domínio de Java e Spring Boot com boas práticas de desenvolvimento, como Clean Code, DDD, SOLID e a separação de responsabilidades em camadas.

---

## 🧠 Architecture Haiku

### 🎯 Objetivos do Negócio  
- Integrar com a API externa da FIPE para obtenção de dados de veículos  
- Implementar um sistema de filas com Kafka para processamento assíncrono  
- Gerenciar o ciclo de vida dos dados (CRUD) com persistência em banco SQL  

### 🔐 Restrições  
- Operação em API REST  
- Foco exclusivo em backend  
- Uso de contêineres para orquestração  

### ⚙️ Atributos de Qualidade  
Escalabilidade > Segurança > Limpeza de Código > Manutenibilidade  

### 🧱 Decisões de Design  
- Java Spring Boot + Maven  
- Estrutura em Módulos: API-1 (produtor), API-2 (consumidor)  
- Estrutura de camadas: Controller, Service, Repository, Domain  
- Tecnologias de apoio: PostgreSQL, Kafka, Redis e Flyway  

---

## 📂 Endpoints da API

### 🔐 Autenticação  
Endpoints de segurança, acessíveis publicamente.

#### **POST** `/v1/api/auth/register`  
```json
{
  "username": "Seu usuário",
  "email": "seuusuario1@email.com",
  "password": "12345678"
}
```

**Response:**  
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userId": "b0b46ade...",
  "username": "Seu usuário",
  "email": "seuusuario1@email.com",
  "role": "USER",
  "expiresAt": "2025-08-25T17:00:52.6332171"
}
```

#### **POST** `/v1/api/auth/login`  
```json
{
  "username": "Seu usuário",
  "password": "12345678"
}
```

**Response:**  
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "userId": "b0b46ade-3dba...",
  "username": "Seu usuário",
  "email": "seuusuario1@email.com",
  "role": "USER",
  "expiresAt": "2025-08-25T17:05:28.2987198"
}
```

---

### 🚘 Gerenciamento de Veículos  
> Todos os endpoints abaixo requerem autenticação com um Bearer Token.

#### **POST** `/v1/api/veiculos/carga-inicial`  
**Response:**  
```text
Carga inicial enviada para processamento.
```

**Descrição:**  
Este endpoint busca as marcas da FIPE e as envia para o Kafka, onde a API de processamento irá consumir e salvar no banco.

---

### 🔄 Endpoints Principais  
| Método | Rota                                      | Descrição                                      |
|--------|-------------------------------------------|------------------------------------------------|
| POST   | `/v1/api/auth/register`                   | Registrar um novo usuário                      |
| POST   | `/v1/api/auth/login`                      | Logar com um usuário existente                 |
| POST   | `/v1/api/veiculos/carga-inicial`          | Disparar a carga inicial de dados da FIPE      |
| GET    | `/v1/api/veiculos/marcas`                 | Listar todas as marcas de veículos salvas      |
| GET    | `/v1/api/veiculos/{idMarca}`              | Buscar veículos de uma marca específica por ID |
| PUT    | `/v1/api/veiculos/{idVeiculo}`            | Atualizar dados de um veículo                  |

---

## 📚 Documentação da API (Swagger)  
Acesse a documentação interativa via Swagger em:  
🔗 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🛠️ Tecnologias Utilizadas  
- Java 17  
- Spring Boot  
- Spring Security  
- PostgreSQL  
- Spring Data JPA  
- Kafka  
- Redis  
- Lombok  
- Maven  
- JUnit 5 + Mockito  
- Flyway  
- Docker e Docker Compose  
- Springdoc OpenAPI (Swagger)  

---

## 🚀 Como Executar  
```bash
git clone https://github.com/mardsantana/Avaliacao-MagnumBK
cd Avaliacao-MagnumBK
docker-compose up -d --build
```

Acesse: [http://localhost:8080](http://localhost:8080)

---

## 🧪 Testes via Postman  
Clique nos botões abaixo para importar as coleções de requisições:

🔐 Coleção de Autenticação  
🔗 [![Run in Postman](https://run.pstmn.io/button.svg)](https://cloudy-meadow-78898.postman.co/collection/24322454-788cfd73-6f28-4818-a926-3b9542cd779d?source=rip_html)  

🚘 Coleção de Veículos e Outros Endpoints  
🔗 [![Run in Postman](https://run.pstmn.io/button.svg)](https://cloudy-meadow-78898.postman.co/collection/24322454-b062c7d7-be5d-4d9b-851d-2db12b1e71d1?source=rip_markdown)

---

## 👨‍💻 Autor  
**Mardson Santos de Santana**  
Desenvolvedor Backend Java  
🔗 [linkedin.com/in/mardson-santana98-java](https://www.linkedin.com/in/mardson-santana98-java/)

