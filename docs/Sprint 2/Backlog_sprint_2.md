# API 2º Semestre Banco de Dados - Siga Feliz

<p align="center">
      <img src="docs\assets\imagens\eagle_logo_2.png" alt="logo Eagle" width="200">
      <h2 align="center">Equipe Eagle</h2>
</p>

---

# Documentação Sprint 2/3

**Período:** 13/04 – 03/05

> Status da Sprint: Aguardando Sprint Review ⏳

---

## 🎯 Desafio
O desafio consiste em criar uma aplicação desktop capaz de auxiliar os docentes no planejamento semestral dos cronogramas acadêmicos. O planejamento semestral é realizado de maneira manual, onde os professores devem cadastrar os temas ministrados garantindo a conformidade institucional com a carga exata de 40 ou 80 aulas e respeitando restrições de calendário (feriados, eventos institucionais e ciclos de Sprints).

## 🏅 Solução
O "Siga Feliz" será responsável por automatizar a distribuição dos temas, facilitando a entrada de dados do usuário e eliminando o esforço cognitivo de se distribuir as aulas manualmente pelo calendário. A solução portará um algoritmo de alocação inteligente e validação de regras de negócio.

## 📋 Sprint Backlog

### Objetivo da Sprint

A segunda SPRINT se concentrou em montar a infra-estrutura da solução, organização do trabalho e na entrega das primeiras funcionalidades.



| Rank | Prioridade | User Story| Story Points |  Sprint   | Requisito do Cliente | Status |
|:----:|:----------:| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | :----------: |:---------:| :------------------: | :----: |
|  1   |    Alta    | Como coordenador, quero fornecer para o professor os parâmetros temporais do semestre regular e o início do Projeto API, para que o planejamento considere simultaneamente os dias úteis e os ciclos contínuos de Sprints.	|      5      | Sprint 2	 |       US01      |    ✅   |
|  2   |    Alta    | Como coordenador, quero criar a grade semanal de uma disciplina e vinculá-la ao perfil de um professor específico, para que o docente acesse e planeje apenas a capacidade real das matérias sob sua responsabilidade.	|      4      |    Sprint 2	     |       US03      |    ✅   |
|  4   |   Média    | Como coordenador, quero registrar o perfil de cada professor no sistema, para que o professor selecione seu registro e visualize somente suas grades. |      4      |    Sprint 2	     |       US02      |    ✅   |
|  8   |   Baixa    | Como professor, quero me identificar no sistema e visualizar apenas as disciplinas atribuídas a mim, para garantir o isolamento dos dados e focar exclusivamente no meu planejamento.|      4      |    Sprint 2	     |       US05      |    ⏳   |
|  9   |   Baixa    | Como coordenador, quero que o sistema projete ciclos contínuos de 28 dias a partir da data do Kickoff, para que as restrições de planejamento e datas de Sprint Review sejam mapeadas automaticamente até o fim do semestre. |      4      |    Sprint 2	     |       US04      |    ⏳   |

## 🏅 DoR - Definition of Ready <a id="dor"></a>

|             Critério					| Descrição                                                                              						|
| :------------------------------:		| -------------------------------------------------------------------------------------------------				|
|  Título Claro   						| É possível entender do que se trata? 																			|
| Regra de Negócios Claras				| Estão definidos os INPUT  e OUTPUTs da User Story?															|
| Compreensão validada c/ time			| Foi realizada reunião com o time para discutir se havia alguma dúvida? 										|
| Sem dependência bloqueadora			| Há algum impedimento para desenvolver? Ex.: desconhecimento da função matemática pelo dev?					|
| Estimado pela equipe					| Apesar de não ser avaliado para nota, decidimos que gostaríamos de já começar estimando o esforço do processo	|


## 🏅 DoD - Definition of Done <a id="dod"></a>

|                 Critério                 | Descrição                                                                           	|
| :--------------------------------------: | ------------------------------------------------------------------------------------	|
|     Código funcionando 				   | Todos os cenários de teste da história foram executados e aprovados.			     	|
|     README atualizado				       | -																						|
|     Código revisado          			   | O código foi revisado por pelo menos um colega de equipe.                          	|

---

## Equipe

<div align="center">
  <table align="center">
    <tr>
      <td align="center" width="250px">
        <img src="/docs/assets/imagens/integrantes/alessandro.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Alessandro Cabral</b><br>
        <i>Product Owner</i><br>
        <a href="https://github.com/alessandrocabralfatec"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/alessandro-augusto-ferreira-cabral-9b805553//"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
      <td align="center" width="250px">
        <img src="/docs/assets/imagens/integrantes/breno.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Breno Cefas</b><br>
        <i>Scrum Master</i><br>
        <a href="https://github.com/cefasbreno"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/breno-cefas-7aa909271/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
      <td align="center" width="250px">
        <img src="/docs/assets/imagens/integrantes/thayssa.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Thayssa Andrade</b><br>
        <i>Desenvolvedora</i><br>
        <a href="https://github.com/Thayssa-Andrade"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/thayssa-andrade/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
    </tr>
    <tr>
      <td align="center" width="250px">
        <img src="/docs/assets/imagens/integrantes/rubens.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Eruano Rubens</b><br>
        <i>Desenvolvedor</i><br>
        <a href="https://github.com/Eruano-Almeida"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/eruano-rubens-de-almeida-b0ba19111/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
      <td align="center" width="250px">
        <img src="/docs/assets/imagens/integrantes/fernando.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Fernando Montero</b><br>
        <i>Desenvolvedor</i><br>
        <a href="https://github.com/fernandocosta45"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="pendente"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
      <td align="center" width="250px">
        <img src="/docs/assets/imagens/integrantes/rafael.png" width="120" height="120" style="border-radius: 10px;"><br>
        <b>Rafael Rodrigues</b><br>
        <i>Desenvolvedor</i><br>
        <a href="https://github.com/Rafael-SantosR"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" width="70"></a>
        <a href="https://www.linkedin.com/in/rafaels-rodrigues/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" width="70"></a>
      </td>
    </tr>
  </table>
</div>


