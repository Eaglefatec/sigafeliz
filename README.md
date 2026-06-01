# API 2º Semestre Banco de Dados - Siga Feliz

<p align="center">
      <img src="docs\assets\imagens\eagle_logo_2.png" alt="logo Eagle" width="200">
      <h2 align="center">Equipe Eagle</h2>
</p>

<!-- TOC -->
* [API 2º Semestre Banco de Dados - Siga Feliz](#api-2º-semestre-banco-de-dados---siga-feliz)
  * [🎯 Desafio](#-desafio)
  * [🏅 Solução](#-solução)
  * [📋 Backlog do Produto](#-backlog-do-produto)
  * [📅 Planejamento de Entregas](#-planejamento-de-entregas)
  * [🗓️ Cronograma de Sprints](#-cronograma-de-sprints)
  * [⚙️ Tecnologias Utilizadas](#-tecnologias-utilizadas)
    * [Estrutura do Projeto](#estrutura-do-projeto)
  * [🚀 Como Testar o Protótipo](#-como-testar-o-protótipo)
    * [Pré-requisitos](#pré-requisitos)
    * [Passo a Passo](#passo-a-passo)
    * [Solução de Problemas](#solução-de-problemas)
  * [Documentação](#documentação)
    * [✅ DoR - Definition of Ready](#-dor---definition-of-ready)
    * [🏁 DoD - Definition of Done](#-dod---definition-of-done)
    * [🌿 Estratégia de Branch](#-estratégia-de-branch)
  * [Equipe](#equipe)
<!-- TOC -->

## 🎯 Desafio
O desafio consiste em criar uma aplicação desktop capaz de auxiliar os docentes no planejamento semestral dos cronogramas acadêmicos. O planejamento semestral é realizado de maneira manual, onde os professores devem cadastrar os temas ministrados garantindo a conformidade institucional com a carga exata de 40 ou 80 aulas e respeitando restrições de calendário (feriados, eventos institucionais e ciclos de Sprints).

## 🏅 Solução
O "Siga Feliz" será responsável por automatizar a distribuição dos temas, facilitando a entrada de dados do usuário e eliminando o esforço cognitivo de se distribuir as aulas manualmente pelo calendário. A solução portará um algoritmo de alocação inteligente e validação de regras de negócio.

## 📋 Backlog do Produto

| Rank | User Story | Prioridade | Dificuldade | Sprint Planejada |
| :---: | --- | :---: | :---: | :---: |
| 1º | US01: Como coordenador, quero fornecer para o professor os parâmetros temporais do semestre regular e o início do Projeto API, para que o planejamento considere simultaneamente os dias úteis e os ciclos contínuos de Sprints. | Alta | Alta |  2 | 
| 2º | US03: Como coordenador, quero criar a grade semanal de uma disciplina e vinculá-la ao perfil de um professor específico, para que o docente acesse e planeje apenas a capacidade real das matérias sob sua responsabilidade. | Alta | Média |  2 | 
| 3º | US06: Como professor, quero planejar a relação entre temas de aula e dias lecionados com o mínimo de input manual e esforço cognitivo possível, para que a distribuição da matéria ao longo do semestre siga as regras institucionais. | Alta | Alta |  3 | 
| 4º | US02: Como coordenador, quero registrar o perfil de cada professor no sistema, para que o professor selecione seu registro e visualize somente suas grades. | Média | Baixa |  2 | 
| 5º | US07: Como professor, quero que o cronograma utilize sábados letivos com prioridade para o final do semestre quando os dias de semana forem insuficientes, garantindo o cumprimento exato da carga horária de 40 ou 80 aulas. | Média | Alta |  3 | 
| 6º | US08: Como professor, quero identificar quais aulas são provas, para que o sistema impeça que elas sejam alocadas nas semanas de entrega de Projeto Integrador, garantindo conformidade com a política institucional de não sobreposição de avaliações. | Média | Média |  3 | 
| 7º | US09: Como professor, quero visualizar a relação do planejamento final em um formato estruturado (.xlsx), para que as informações de data, tema e ordem sejam facilmente preenchidas no SIGA. | Média | Baixa |  3 | 
| 8º | US05: Como professor, quero me identificar no sistema e visualizar apenas as disciplinas atribuídas a mim, para garantir o isolamento dos dados e focar exclusivamente no meu planejamento. | Baixa | Baixa |  2 | 
| 9º | US04: Como coordenador, quero que o sistema projete ciclos contínuos de 28 dias a partir da data do Kickoff, para que as restrições de planejamento e datas de Sprint Review sejam mapeadas automaticamente até o fim do semestre. | Baixa | Média |  2 | 


## 📅 Planejamento de Entregas
O projeto será dividido em três fases principais de desenvolvimento:

* **Sprint 1:** Foco em negociação com o cliente, discussões técnicas internas, formulação da solução e modelagem de dados. Esta etapa priorizou a estruturação do projeto e a execução de protótipos, sem o desenvolvimento de User Stories (US) operacionais.
* **Sprint 2:** Desenvolvimento da infraestrutura de cadastro dos parâmetros de calendário e configurações da coordenação. Aqui também se planeja o início dos algoritmos de distribuição do planejamento ao longo do semestre.
* **Sprint 3:** Foco total na implementação das funcionalidades voltadas aos perfis de professor usuário, refinamentos de interface e finalização do algoritmo de alocação.

## 🗓️ Cronograma de Sprints

| Sprint | Periodo | Documentação | Vídeo |
| :---: | :---: | :---: | :---: |
| Sprint 1 | 16/03 - 05/04 | [Sprint 1 Docs](/docs/Sprint%201/) | [Demonstração Protótipo](/docs/Sprint%201/prototipo.mp4) |
| Sprint 2 | 13/04 - 03/05 | [Sprint 2 Docs](/docs/Sprint%202/) | [Demonstração Protótipo](https://youtu.be/x2odyvxCNWA)|
| Sprint 3 | 11/05 - 31/05 | [Sprint 3 Docs](/docs/Sprint%203/) | [Demonstração Protótipo](https://youtu.be/5BWaB5uCDCM) |

## ⚙️ Tecnologias Utilizadas

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/javafx-%23FF0000.svg?style=for-the-badge&logo=javafx&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Jira](https://img.shields.io/badge/jira-%230A0FFF.svg?style=for-the-badge&logo=jira&logoColor=white)

### Estrutura do Projeto

```
sigafeliz/                  ← raiz do repositório
├── docs/                   ← documentação do projeto
└── sigafeliz/              ← projeto Maven (abra esta pasta na IDE)
    ├── pom.xml
    └── src/
        └── main/
            ├── java/com/sigafeliz/
            │   ├── Main.java           ← ponto de entrada da aplicação
            │   ├── controller/         ← controladores JavaFX (FXML)
            │   ├── dao/                ← conexao entre model e infra (CRUD BANCO)
            │   ├── infra/              ← conexao com o banco de dados (FXML)
            │   ├── model/              ← entidades e modelos de dados
            │   └── service/            ← regras de negócio
            └── resources/
                ├── view/               ← arquivos .fxml (telas)
                ├── secret/             ← db.properties (conexao com POSTGRES)
                ├── db/                 ← arquivos .sql (schemas para criar as tabelas SQL)
                └── css/                ← estilos da aplicação
                
```

## 🚀 Como Testar o Protótipo

### Pré-requisitos

Antes de começar, verifique se você tem os seguintes itens instalados na sua máquina:

| Ferramenta | Versão mínima | Download |
|---|---|---|
| **Java JDK** | 17 | [Azul Zulu 17](https://www.azul.com/downloads/?version=java-17&os=windows&package=jdk) ou [Oracle JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) |
| **Apache Maven** | 3.8+ | [maven.apache.org](https://maven.apache.org/download.cgi) |

Os comandos a seguir podem ser feitos em terminais como `PowerShell` ou `Bash`.
> **Como verificar se já estão instalados:**
> ```bash
> java -version   # deve mostrar algo como: java version "17.x.x"
> mvn -version    # deve mostrar algo como: Apache Maven 3.x.x
> ```

### Passo a Passo

**1. Clone o repositório**
```bash
git clone https://github.com/Eaglefatec/sigafeliz.git
cd sigafeliz
```

**2. Navegue até a pasta do projeto Java**
```bash
cd sigafeliz
```
> ⚠️ Atenção: a pasta do projeto Maven fica dentro de uma subpasta também chamada `sigafeliz`. Não se confunda com a pasta raiz do repositório.

**3. Compile e execute a aplicação (primeira vez)**
```bash
mvn clean javafx:run
```

Aguarde o Maven baixar as dependências (somente na primeira vez) e compilar. A janela do **Siga Feliz** abrirá automaticamente.

**Execuções seguintes (sem alterações no código)**

Após a primeira compilação, o `clean` é desnecessário. Basta rodar direto pelo terminal:
```bash
mvn javafx:run
```

### Solução de Problemas

| Problema                                     | Causa provável                        | Solução                                                                                                                                                                                           |
|----------------------------------------------|---------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `java version` mostra versão diferente de 17 | Versão errada no PATH                 | Configure a variável `JAVA_HOME` apontando para o JDK 17                                                                                                                                          |
| `mvn` não é reconhecido                      | Maven não está no PATH                | Adicione `<pasta_maven>/bin` na variável de ambiente `PATH`                                                                                                                                       |
| Janela não abre mas o build é `SUCCESS`      | Ambiente sem display gráfico          | Execute em uma máquina com interface gráfica                                                                                                                                                      |
| Erro de conexão com o banco de dados         | Credenciais do banco não configuradas | Acesse a pasta **/sigafeliz/sigafeliz/src/main/resources/secret** e crie um arquivo com o nome **db.properties**. Preencha os dados de acesso igual ao template do arquivo **db.properties.mock** |

## Documentação

### ✅ DoR - Definition of Ready

Para que uma User Story seja considerada pronta para desenvolvimento, os seguintes critérios devem ser cumpridos:
* A User Story possui um título claro, descrição bem definida (formato "Como... quero... para...") e objetivo compreendido.
* Há wireframes e/ou imagens de protótipos das interfaces relacionadas.
* Os Critérios de Aceitação estão escritos e detalhados.
* As regras de negócio associadas estão claras e documentadas.
* Não há dependências bloqueadoras.
* A compreensão foi validada com o time.

### 🏁 DoD - Definition of Done

Para que uma User Story seja considerada finalizada, os seguintes critérios técnicos assumidos pela equipe devem ser satisfeitos:
* Código devidamente versionado no Git.
* Código revisado pela equipe.
* Todos os casos de uso relacionados à história foram testados, conforme levantados pela equipe.

### 🌿 [Estratégia de Branch](docs/equipe/Estratégia%20Branches.md/)

Definição de como as branches serão trabalhadas durante o projeto.



## Modelo Entidade Relacionamento <a id="modelo"></a>
<details closed>
<summary>
 Modelo Entidade Relacionamento
</summary> <br />

<p align="center">

DER - Semestre

![DeR_Semestre.png](/docs/assets/imagens/diagramas/DeR_Semestre.png)
</p>

<p align="center">

DER - Professor

![DeR_Semestre.png](/docs/assets/imagens/diagramas/DeR_Professor.png)
</p>

</details>

## Equipe

<div align="center">
  <table align="center">
    <tr>
      <td align="center" width="250px">
        <img src="docs/assets/imagens/integrantes/alessandro.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Alessandro Cabral</b><br>
        <i>Product Owner</i><br>
        <a href="https://github.com/alessandrocabralfatec"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/alessandro-augusto-ferreira-cabral-9b805553//"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
      <td align="center" width="250px">
        <img src="docs/assets/imagens/integrantes/breno.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Breno Cefas</b><br>
        <i>Scrum Master</i><br>
        <a href="https://github.com/cefasbreno"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/breno-cefas-7aa909271/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
      <td align="center" width="250px">
        <img src="docs/assets/imagens/integrantes/thayssa.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Thayssa Andrade</b><br>
        <i>Desenvolvedora</i><br>
        <a href="https://github.com/Thayssa-Andrade"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/thayssa-andrade/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
    </tr>
    <tr>
      <td align="center" width="250px">
        <img src="docs/assets/imagens/integrantes/rubens.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Eruano Rubens</b><br>
        <i>Desenvolvedor</i><br>
        <a href="https://github.com/Eruano-Almeida"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/eruano-rubens-de-almeida-b0ba19111/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
      <td align="center" width="250px">
        <img src="docs/assets/imagens/integrantes/fernando.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Fernando Montero</b><br>
        <i>Desenvolvedor</i><br>
        <a href="https://github.com/fernandocosta45"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="pendente"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
      <td align="center" width="250px">
        <img src="docs/assets/imagens/integrantes/rafael.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Rafael Rodrigues</b><br>
        <i>Desenvolvedor</i><br>
        <a href="https://github.com/Rafael-SantosR"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/rafaels-rodrigues/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
    </tr>
  </table>
</div>


