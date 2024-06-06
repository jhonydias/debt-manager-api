# Debt Manager API

Debt Manager API é um projeto Spring Boot para gerenciar dívidas, incluindo funcionalidades para criação de dívidas, registro de pagamentos, verificação de status de dívidas e mais. Este projeto utiliza Hibernate, JPA, ShedLock e outras tecnologias para garantir um funcionamento robusto e escalável.

## Funcionalidades

- **Criação de Dívidas**: Endpoint para criar novas dívidas.
- **Registro de Pagamentos**: Endpoint para registrar pagamentos.
- **Verificação de Status de Dívidas**: Scheduler que verifica e atualiza o status das dívidas diariamente.
- **Paginação e Busca**: Métodos para buscar dívidas com paginação e filtros.

## Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- ShedLock
- PostgreSQL
- Docker
- Lombok

## Configuração do Projeto
- **JDK 17**
- **Maven 3.6+**
- **Docker**

### Instalação

Clone o repositório em sua máquina local usando:
```bash
git clone https://github.com/jhonydias/debt-manager-api.git
cd debt-manager-api
````
## Executando o Projeto
Siga os passos abaixo para construir e executar o projeto:

### Construa o projeto com Maven:

```sh
./mvnw clean package -DskipTests
```
### Construa o projeto com Docker Compose:
```sh
docker-compose up --build
```
### Construa o projeto com [build_and_clean.sh](build_and_clean.sh):
- Permissão de Execução para o Script
```sh
chmod +x run_and_clean.sh
```
- Executar o Script
```sh
./run_and_clean.sh
```
### Uso
Após iniciar o serviço, ele estará disponível para receber requisições HTTP.

## Documentação da API

A documentação interativa da API está disponível através do Swagger UI. Isso permite que você visualize e interaja com a API diretamente do seu navegador.

### Acessar Swagger UI

Para acessar a documentação da API, visite o seguinte link enquanto o servidor estiver em execução:

[Swagger UI](http://localhost:8081/swagger-ui.html)

Certifique-se de que o serviço esteja rodando e acessível na porta especificada para utilizar a Swagger UI.

