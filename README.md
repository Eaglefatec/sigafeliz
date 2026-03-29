# API 2º Semestre Banco de Dados

<p align="center">
      <img src="docs\assets\eagle_logo_2.png" alt="logo Eagle" width="200">
      <h2 align="center">Eagle</h2>
</p>

## Desafio
O desafio consiste em criar uma aplicação desktop capaz de auxiliar os docentes no planejamento semestral dos cronogramas acadêmicos. O planejamento semestral é realizado de maneira manual, onde os professores devem cadastrar os temas ministrados garantindo a conformidade institucional com a carga exata de 40 ou 80 aulas e respeitando restrições de calendário (feriados, eventos institucionais e ciclos de Sprints).

## Solução
O "Siga Feliz" será responsável por automatizar a distribuição dos temas, facilitando a entrada de dados do usuário e eliminando o esforço cognitivo de se distribuir as aulas manualmente pelo calendário. A solução portará um algoritmo de alocação inteligente e validação de regras de negócio.

## Backlog do Produto

| Rank | User Story | Prioridade | Dificuldade | Sprint Planejada | Status |
| :---: | --- | :---: | :---: | :---: | :---: |
| 1 | Como coordenador, quero fornecer para o professor os parâmetros temporais do semestre regular e o início do Projeto API, para que o planejamento considere simultaneamente os dias úteis e os ciclos contínuos de Sprints. | Alta | Baixa | Sprint 3 | |
| 2 | Como coordenador, quero registrar o perfil de cada professor no sistema, para que o professor selecione seu registro e visualize somente suas grades. | Alta | Baixa | Sprint 3 | |
| 3 | Como coordenador, quero criar a grade semanal de uma disciplina e vinculá-la ao perfil de um professor específico, para que o docente acesse e planeje apenas a capacidade real das matérias sob sua responsabilidade. | Alta | Média | Sprint 3 | |
| 4 | Como coordenador, quero que o sistema projete ciclos contínuos de 28 dias a partir da data do Kickoff, para que as restrições de planejamento e datas de Sprint Review sejam mapeadas automaticamente até o fim do semestre. | Alta | Média | Sprint 2 | |
| 5 | Como professor, quero me identificar no sistema e visualizar apenas as disciplinas atribuídas a mim, para garantir o isolamento dos dados e focar exclusivamente no meu planejamento. | Alta | Alta | Sprint 3 | |
| 6 | Como professor, quero planejar a relação entre temas de aula e dias lecionados com o mínimo de input manual e esforço cognitivo possível, para que a distribuição da matéria ao longo do semestre siga as regras institucionais. | Média | Baixa | Sprint 2 | |
| 7 | Como professor, quero que o cronograma utilize sábados letivos com prioridade para o final do semestre quando os dias de semana forem insuficientes, garantindo o cumprimento exato da carga horária de 40 ou 80 aulas. | Média | Alta | Sprint 3 | |
| 8 | Como professor, quero identificar quais aulas são provas, para que o sistema impeça que elas sejam alocadas nas semanas de entrega de Projeto Integrador, garantindo conformidade com a política institucional de não sobreposição de avaliações. | Média | Média | Sprint 3 | |
| 9 | Como professor, quero visualizar a relação do planejamento final em um formato estruturado (.xlsx), para que as informações de data, tema e ordem sejam facilmente preenchidas no SIGA. | Baixa | Baixa | Sprint 3| |

## DoR - Definition of Ready

Para que uma User Story seja considerada pronta para desenvolvimento, os seguintes critérios devem ser cumpridos:
* A User Story possui um título claro, descrição bem definida (formato "Como... quero... para...") e objetivo compreendido.
* Há wireframes e/ou imagens de protótipos das interfaces relacionadas.
* Os Critérios de Aceitação estão escritos e detalhados.
* As regras de negócio associadas estão claras e documentadas.
* Não há dependências bloqueadoras.
* A compreensão foi validada com o time.

## DoD - Definition of Done

Para que uma User Story seja considerada finalizada, os seguintes critérios técnicos assumidos pela equipe devem ser satisfeitos:
* Código devidamente versionado no Git.
* Código revisado pela equipe.
* Todos os casos de uso relacionados à história foram testados, conforme levantados pela equipe.

## Cronograma de Sprints

| Sprint | Periodo | Documentação |
| :---: | :---: | :---: |
| Sprint 1 | 16/03 - 05/04 | Sprint 1 Docs |
| Sprint 2 | 13/04 - 03/05 | Sprint 2 Docs |
| Sprint 3 | 11/05 - 31/05 | Sprint 3 Docs |

## Tecnologias Utilizadas

## Manual de Instalação

## Equipe

<div align="center">
  <table>
    <tr>
      <th>Membro</th>
      <th>Função</th>
      <th>Github</th>
      <th>Linkedin</th>
    </tr>
    <tr>
      <td>Guilherme Ioshua Sene</td>
      <td>Product Owner</td>
      <td><a href="https://github.com/guiioshua"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"></a></td>
      <td><a href="https://www.linkedin.com/in/guilherme-ioshua-sene/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"></a></td>
    </tr>
    <tr>
      <td>Breno Cefas dos Santos</td>
      <td>Scrum Master</td>
      <td><a href="https://github.com/cefasbreno"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"></a></td>
      <td><a href="https://www.linkedin.com/in/breno-cefas-7aa909271/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"></a></td>
    </tr>
    <tr>
      <td>Alessandro Augusto Ferreira Cabral</td>
      <td>Desenvolvedor</td>
      <td><a href="https://github.com/alessandrocabralfatec"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"></a></td>
      <td><a href="https://www.linkedin.com/in/alessandro-augusto-ferreira-cabral-9b805553//"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"></a></td>
    </tr>
    <tr>
      <td>Eruano Rubens de Almeida</td>
      <td>Desenvolvedor</td>
      <td><a href="https://github.com/Eruano-Almeida"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"></a></td>
      <td><a href="https://www.linkedin.com/in/eruano-rubens-de-almeida-b0ba19111/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"></a></td>
    </tr>
    <tr>
      <td>Fernando Montero da Costa</td>
      <td>Desenvolvedor</td>
      <td><a href="https://github.com/fernandocosta45"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"></a></td>
      <td><a href="pendente"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"></a></td>
    </tr>
    <tr>
      <td>Renan Diniz da Silva</td>
      <td>Desenvolvedor</td>
      <td><a href="https://github.com/renandiniz8"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white"></a></td>
      <td><a href="https://www.linkedin.com/in/renan-diniz-/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white"></a></td>
    </tr>
  </table>
</div>