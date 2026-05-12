# API 2º Semestre Banco de Dados - Siga Feliz

<p align="center">
      <img src="/docs\assets\imagens\eagle_logo_2.png" alt="logo Eagle" width="200">
      <h2 align="center">Equipe Eagle</h2>
</p>

---

# Documentação Sprint 3

**Período:** 11/05 – 31/05

> Status da Sprint: Em andamento

---

## 🎯 Desafio
O desafio consiste em criar uma aplicação desktop capaz de auxiliar os docentes no planejamento semestral dos cronogramas acadêmicos. O planejamento semestral é realizado de maneira manual, onde os professores devem cadastrar os temas ministrados garantindo a conformidade institucional com a carga exata de 40 ou 80 aulas e respeitando restrições de calendário (feriados, eventos institucionais e ciclos de Sprints).

## 🏅 Solução
O "Siga Feliz" será responsável por automatizar a distribuição dos temas, facilitando a entrada de dados do usuário e eliminando o esforço cognitivo de se distribuir as aulas manualmente pelo calendário. A solução portará um algoritmo de alocação inteligente e validação de regras de negócio.

## 📋 Sprint Backlog

### Objetivo da Sprint

A terceira SPRINT focará em entregar as funções de PROFESSOR, principal usuário da aplicação desenvolvida. A entrega ao final deve gerar para uso uma planilha com os dados inseridos pelo professor e trabalhados pela solução, facilitando a entrada de dados na plataforma SIGA.

- [Vídeo de Demonstração do Protótipo]()

**Meta da Sprint:** US06, US07, US08, US09, US04, US05 

|  ID  | Prioridade | User Story| Story Points | Sprint |          Status           |
|:----:|:----------:| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |:------------:|:------:|:-------------------------:|
| [US06](/docs/projeto/User_Stories/US06.md) |    Alta    | Como professor, quero planejar a relação entre temas de aula e dias lecionados com o mínimo de input manual e esforço cognitivo possível, para que a distribuição da matéria ao longo do semestre siga as regras institucionais.	|      13       |   3	   |             ⏳             |
| [US07](/docs/projeto/User_Stories/US07.md) |    Alta    | Como professor, quero que o cronograma utilize sábados letivos com prioridade para o final do semestre quando os dias de semana forem insuficientes, garantindo o cumprimento exato da carga horária de 40 ou 80 aulas.	|      5       |   3	   |             ⏳             |
| [US08](/docs/projeto/User_Stories/US08.md) |   Média    | Como professor, quero identificar quais aulas são provas, para que o sistema impeça que elas sejam alocadas nas semanas de entrega de Projeto Integrador, garantindo conformidade com a política institucional de não sobreposição de avaliações. |      8       |   3	   |             ⏳             |
| [US09](/docs/projeto/User_Stories/US09.md) |   Média    | Como professor, quero visualizar a relação do planejamento final em um formato estruturado (.xlsx), para que as informações de data, tema e ordem sejam facilmente preenchidas no SIGA. |      8       |   3	   |             ⏳             |
| [US05](/docs/projeto/User_Stories/US05.md) |   Baixa    | Como professor, quero me identificar no sistema e visualizar apenas as disciplinas atribuídas a mim, para garantir o isolamento dos dados e focar exclusivamente no meu planejamento.|      3       |   3	   | ⏳ |
| [US04](/docs/projeto/User_Stories/US04.md) |   Baixa    | Como coordenador, quero que o sistema projete ciclos contínuos de 28 dias a partir da data do Kickoff, para que as restrições de planejamento e datas de Sprint Review sejam mapeadas automaticamente até o fim do semestre. |      5       |   3	   | ⏳ |


--------

## 🏃‍ DoR - Definition of Ready <a id="dor"></a>

- [x] A User Story possui um título claro, descrição bem definida (formato "Como... quero... para...") e objetivo compreendido.
- [x] Há wireframes e/ou imagens de protótipos das interfaces relacionadas.
- [x] Os Critérios de Aceitação estão escritos e detalhados.
- [x] As regras de negócio associadas estão claras e documentadas.
- [x] Não há dependências bloqueadoras.
- [x] A compreensão foi validada com o time.

## 🏆 DoD - Definition of Done <a id="dod"></a>
- [ ] Código devidamente versionado no Git.
- [ ] Código revisado pela equipe.
- [ ] Todos os casos de uso relacionados à história foram testados, conforme levantados pela equipe.

