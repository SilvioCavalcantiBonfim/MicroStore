# Backend Java com Microsserviços, Spring Boot e Kubernetes

![docker](https://img.shields.io/badge/-Docker-white?style=for-the-badge&logo=docker&color=2496ED&logoColor=white)
![kubernetes](https://img.shields.io/badge/-Kubernetes-white?style=for-the-badge&logo=kubernetes&color=326CE5&logoColor=white)
![spring](https://img.shields.io/badge/-Spring-white?style=for-the-badge&logo=spring&color=6DB33F&logoColor=white)
![postgresql](https://img.shields.io/badge/-PostgreSQL-white?style=for-the-badge&logo=postgresql&color=4169E1&logoColor=white)
![flyway](https://img.shields.io/badge/-Flyway-white?style=for-the-badge&logo=flyway&color=CC0200&logoColor=white)

Este repositório é um projeto prático baseado no livro **Backend Java: Microsserviços, Spring Boot e Kubernetes**, escrito por *Eduardo Felipe Zambom Santana*.

## Descrição

O projeto tem como objetivo demonstrar uma aplicação backend desenvolvida em Java, enfatizando a criação e integração de microsserviços por meio do Spring Boot e Kubernetes. A aplicação consiste em três microsserviços distintos:

`User-API`: Gerencia todas as operações relacionadas aos usuários.

`Product-API`: Responsável por todas as tarefas relacionadas aos produtos.

`Shopping-API`: Administra todas as atividades relacionadas às compras.

Inicialmente, esses microsserviços são independentes e, ao longo do desenvolvimento, foi implementada a integração entre eles, permitindo a comunicação via Docker e a execução da aplicação em um cluster Kubernetes. As imagens de cada microsserviço podem ser encontradas no [docker hub](https://hub.docker.com/r/silviocavalcanti/store). Além disso, o projeto destaca a importância dos Testes de Unidade para garantir a confiabilidade e qualidade da aplicação.

## Principais Atualizações e Implementações

- *Projeto Maven multimódulo:* Organização e modularização do código para melhor manutenção, testes e escalabilidade.
- *Utilização de records para DTOs:* Abordagem concisa e legível na definição de classes imutáveis.
- *Tratamento de exceções global:* Uniformidade e consistência no tratamento de erros.
- *Substituição de `java.util.Date` por `java.time`:* Utilização de API moderna para manipulação de datas e horas.
- *Ofuscação de dados sensíveis:* Reforço na segurança da aplicação.
- *Substituição de `RestTemplate` por `RestClient`:* Abordagem não bloqueante para chamadas de API.
- *Criação de testes unitários e de integração com `AssertJ`:* Garantia de estabilidade e confiabilidade do código.
- *Uso do Jacoco para cobertura de código:* Medição eficaz da cobertura de código.
- *Jakarta Validation para validação nos DTOs de entrada:* Garantia de integridade dos dados.

## Planos Futuros

- Desenvolvimento de um validador de CPF no módulo `utils`.
- Substituição do RestClient no Shopping-api por gRPC.
- Implementação de um serviço de autenticação e autorização com JWT.
- Criação de imagens docker personalizadas com JVMs minimalistas personalizadas.

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.2.0
- PostgreSQL 15.5
- Flyway 9.22.3
- Kubernetes 1.28
- Docker 24.0.7

## Como Executar

1. Para utilizar o Kubernetes, é necessário ter o Minikube instalado. 
2. Execute o comando `minikube start` para iniciar o cluster do Minikube.
3. Navegue até o diretório `k8s`.
4. Execute o script `./create_resources.sh`.

## Contribuições

Contribuições são sempre bem-vindas. Sinta-se à vontade para abrir uma issue ou enviar um pull request.

## Licença

Este projeto está sob a licença MIT. Consulte o arquivo [LICENSE](./LICENSE) para mais detalhes.