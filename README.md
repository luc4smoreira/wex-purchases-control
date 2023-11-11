# Wex Purchases Control
Autor: Lucas Moreira Carneiro de Miranda

O objetivo deste projeto é implementar uma aplicação em Java que expõe um microserviço REST com dois endpoints:





## List of Decisions Made

### 1. Do not round inputted monetary information (Purchase transactions)

To avoid issues related to rounding, the purchase transaction validation expects the monetary values to be already rounded.

### 2. Spring and Microservices

The choice to use the Spring framework and Spring Boot is due to their robustness and industry-standard status in Java backend development. They simplify the process by bundling the project and its dependencies into a single executable file, thus enhancing reliability and modularity. This setup is ideal for deploying scalable microservices and ensures seamless integration with tools such as Docker and Kubernetes.


### 3. Allow get Purchase by id only 

Permitir que seja obtida compra somente no valor original em USD


### 4. Banco de dados e SQL

Foi tomada a decisão de utilizar SQL para maior controle dos dados persistidos. Essa decisão foi motivada pelos seguintes motivos:

- Impedir qualquer mudança no banco de dados não planejada
- Garantir a integridade e qualidade dos dados gravados





Foi presumido que todas as requisições utilizam um timezone padrão de referência, não delegando À aplicação a necessidade de tratar conversões de datas com base em fuso horários



DATA data taxa de câmbio utilizada


O projeto foi feito utilizando
Spring Boot
JDK 20
MySQL
Docker e docker-compose




Comando apra iniciar a aplicação:
1) Certifique-se de ter o Docker disponível
2) Configure o arquivo .env usando o arquivo .env.example de referência 
3) Execute o comando: docker-compose up
4) A configuração do docker-compose define que o banco de dados precisa estar acessível para então iniciar a aplicação. Por isso, pode ser necessário esperar alguns segundos adicionais para que o wex-purchases-control-app seja iniciado




Exemplo de arquivo .env

MYSQLDB_USER=lucas
MYSQLDB_PASSWORD=miranda
MYSQLDB_ROOT_PASSWORD=lucas.miranda
MYSQLDB_DATABASE=wex-purchases-db
MYSQLDB_DOCKER_PORT=3306
APP_DOCKER_PORT=8080
APP_EXTERNAL_PORT=8080