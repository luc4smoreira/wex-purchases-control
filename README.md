# Wex Purchases Control
Author: Lucas Moreira Carneiro de Miranda

This project aims to ...

## List of Decisions Made

### 1. Do not round inputted monetary information (Purchase transactions)

To avoid issues related to rounding, the purchase transaction validation expects the monetary values to be already rounded.

### 2. Spring and Microservices

The choice to use the Spring framework and Spring Boot is due to their robustness and industry-standard status in Java backend development. They simplify the process by bundling the project and its dependencies into a single executable file, thus enhancing reliability and modularity. This setup is ideal for deploying scalable microservices and ensures seamless integration with tools such as Docker and Kubernetes.


### 3. Allow get Purchase by id only 
