# Back-end Java: Microsserviços, Spring Boot e Kubernetes

Este repositório é dedicado à implementação dos conceitos estudados no livro "Back-end Java: Microsserviços, Spring Boot e Kubernetes" de *Eduardo Felipe Zambom Santana*.

# Sobre o Projeto

Este projeto é uma aplicação prática dos conceitos apresentados no livro "Back-end Java: Microsserviços, Spring Boot e Kubernetes". O objetivo é desenvolver uma aplicação back-end robusta usando Java, com foco na criação de microsserviços utilizando Spring Boot e Kubernetes.

A aplicação consiste em três microsserviços distintos, cada um com suas próprias responsabilidades: 

- `Usuários`: Gerencia todas as operações relacionadas aos usuários.
- `Produtos`: Lida com todas as tarefas relacionadas aos produtos.
- `Compras`: Administra todas as atividades relacionadas às compras.

Inicialmente, esses microsserviços são independentes. No decorrer do projeto, trabalhamos na integração desses serviços, permitindo que eles se comuniquem entre si. Para isso, criamos imagens Docker para cada serviço e, finalmente, executamos a aplicação inteira em um cluster Kubernetes.

Este projeto também enfatiza a importância dos Testes de Unidade para garantir a confiabilidade e a qualidade da aplicação. Além disso, introduzimos o `api-gateway`, uma nova funcionalidade que facilita o acesso aos microsserviços.

Para garantir que estamos usando as tecnologias mais recentes, as versões do Spring Boot, Java e Kubernetes foram atualizadas para as mais recentes disponíveis.


## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.0
- Kubernetes 1.28
- Docker 24.0.7

## Como Executar

1. Clone este repositório
2. Navegue até a pasta do projeto
3. Execute o comando `mvn spring-boot:run` para iniciar a aplicação Spring Boot
4. Para o Kubernetes, você precisará ter o Minikube instalado. Execute o comando `minikube start` para iniciar o cluster do Minikube.

## Contribuições

Contribuições são sempre bem-vindas. Sinta-se à vontade para abrir uma issue ou enviar um pull request.

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.
