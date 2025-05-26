# Sistema de Gerenciamento de Estoque

Este é um sistema de gerenciamento de estoque desenvolvido com Angular para o frontend e Java (Spring Boot) para o backend.

## Visão Geral

O sistema permite o controle de produtos em estoque, incluindo funcionalidades como:

- Cadastro de produtos
- Visualização de produtos
- Atualização de informações de produtos
- Exclusão de produtos
- Controle de entrada e saída de estoque
- Relatórios básicos de estoque

## Tecnologias Utilizadas

**Frontend:**

- HTML
- CSS
- TypeScript
- Angular

**Backend:**

- Java
- Spring Boot
- H2

## Pré-requisitos

Para executar este projeto, você precisará ter instalado no seu computador:

- Node.js e npm
- Angular CLI
- Java JDK
- Maven

## Como Executar o Projeto

Siga os passos abaixo para configurar e executar o projeto:

### 1. Clonar o Repositório

```bash
git clone https://github.com/sermoud/stock-control-management
cd stock-control-management
```

### 2. Configurar o Backend

1. Navegue até a pasta do backend: `cd backend`
2. Compile e execute o backend usando Maven:
   ```
   mvn spring-boot:run
   ```

### 3. Configurar e Executar o Frontend

1. Navegue até a pasta do frontend: `cd frontend`
2. Instale as dependências:
   ```
   npm install
   ```
3. Execute o servidor de desenvolvimento do Angular:
   ```
   ng serve
   ```
4. O aplicativo estará disponível em `http://localhost:4200/`

## Licença

Este projeto está licenciado sob a licença MIT

## Contato

Se você tiver alguma dúvida ou sugestão, entre em contato com miltonsermoudneto@gmail.com
