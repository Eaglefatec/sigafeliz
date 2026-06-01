# Documentação de Requisitos: Siga Feliz

### 1.1 Definition of Ready (DoR)
Para que uma User Story seja considerada pronta para desenvolvimento, os seguintes critérios devem ser cumpridos:
* A User Story possui um título claro, descrição bem definida (formato "Como... quero... para...") e objetivo compreendido.
* Há wireframes e/ou imagens de protótipos das interfaces relacionadas.
* Os Critérios de Aceitação estão escritos e detalhados.
* As regras de negócio associadas estão claras e documentadas.
* Não há dependências bloqueadoras.
* A compreensão foi validada com o time.

### 1.2 Definition of Done (DoD)
Para que uma User Story seja considerada finalizada, os seguintes critérios técnicos assumidos pela equipe devem ser satisfeitos:
* Código devidamente versionado no Git.
* Código revisado pela equipe.
* Todos os casos de uso relacionados à história foram testados, conforme levantados pela equipe.

---

## 2. Histórias de Usuário

### 2.1 Priorização de Histórias

| Rank | User Story | Prioridade | Dificuldade | Sprint Planejada |
| :--- | :--- | :--- | :--- | :--- |
| 1º | US01: Definição de Calendário Acadêmico do Semestre | Alta | Alta | Sprint 2 |
| 2º | US03: Definição de Grade de Disciplina e Vínculo Docente | Alta | Média | Sprint 2 |
| 3º | US06: Distribuição Automática de Conteúdo | Alta | Alta | Sprint 3 |
| 4º | US02: Cadastro de Identidade Docente | Média | Baixa | Sprint 2 |
| 5º | US07: Compensação de Carga Horária aos Sábados | Média | Alta | Sprint 3 |
| 6º | US08: Proteção Cíclica de Datas de Avaliação | Média | Média | Sprint 3 |
| 7º | US09: Geração de modelo para preenchimento | Média | Baixa | Sprint 3 |
| 8º | US05: Identificação de Usuário e Seleção de Disciplina | Baixa | Baixa | Sprint 2 |
| 9º | US04: Estabelecimento do Ciclo de Sprints | Baixa | Média | Sprint 2 |

### 2.2 Coordenador Acadêmico

**US01: Definição de Calendário Acadêmico do Semestre**
### HISTÓRIA PRINCIPAL DO COORDENADOR
Como coordenador, quero fornecer para o professor os parâmetros temporais do semestre regular e o início do Projeto API, para que o planejamento considere simultaneamente os dias úteis e os ciclos contínuos de Sprints.
* **Critérios de Aceitação:**
    * Definição de **data de início** e **término** do período letivo.
    * Identificação de dias específicos como **sem aula** (feriados e eventos).
    * Definição da SEMANA do Kickoff do Projeto Integrador. A partir dele, deve haver um período de 28 (dias) x 3 (Sprints) que caiba dentro do semestre. O dia do Kickoff deve ser depois da data inicial do semestre. O último dia da terceira Sprint deve ser anterior a data final do semestre.
    * Cálculo automático do saldo líquido de dias úteis disponíveis na grade regular.

**US02: Cadastro de Identidade Docente**
Como coordenador, quero registrar o perfil de cada professor no sistema, para que o professor selecione seu registro e visualize somente suas grades.
* **Critérios de Aceitação:**
    * Inserção de dados básicos de identificação do professor (ex: Nome e E-mail/Matrícula).
    * Criação de um registro único para cada docente que servirá de base para a etapa de identificação no aplicativo.

**US03: Definição de Grade de Disciplina e Vínculo Docente**
Como coordenador, quero criar a grade semanal de uma disciplina e vinculá-la ao perfil de um professor específico, para que o docente acesse e planeje apenas a capacidade real das matérias sob sua responsabilidade.
* **Critérios de Aceitação:**
    * Seleção de um professor previamente cadastrado (US02).
    * Inserção do nome da disciplina (ex: "Banco de Dados 1").
    * Seleção dos dias da semana em que há aulas regulares para esta disciplina.
    * Definição da quantidade de aulas por dia selecionado.
    * Vinculação da disponibilidade ao total de aulas obrigatórias da disciplina (40 ou 80).

**US04: Estabelecimento do Ciclo de Sprints - [CANCELADA]**

MOTIVO do CANCELAMENTO: A User Story 01 já exige a definição de uma data de Kickoff que prevê o ciclo dos 28 dias.


### 2.3 Professor

**US05: Identificação de Usuário e Seleção de Disciplina**
Como professor, quero me identificar no sistema e visualizar apenas as disciplinas atribuídas a mim, para garantir o isolamento dos dados e focar exclusivamente no meu planejamento.
* **Critérios de Aceitação:**
    * Tela inicial solicitando a identificação do usuário (seja por seleção em lista ou autenticação simples por e-mail/senha, a ser definido na arquitetura final).
    * Após identificação, exibição de uma lista contendo apenas as disciplinas (e suas respectivas grades) vinculadas àquele professor pela coordenação.
    * Restrição sistêmica que impeça a visualização ou edição de disciplinas atribuídas a outros docentes.

**US06: Planejamento do Conteúdo Programático da Disciplina**
Como professor, quero planejar a relação entre temas de aula e dias lecionados com o mínimo de input manual e esforço cognitivo possível, para que a distribuição da matéria ao longo do semestre siga as regras institucionais.
* **Critérios de Aceitação:**
    * A interface deve deixar claro qual disciplina está sendo planejada no momento (selecionada na US05).
    * Cada tema deve ter: título, carga mínima, carga máxima, nível de prioridade e PROVA (sim ou não).
    * Garantia de que o valor mínimo de todos os temas seja alocado primeiro.
    * Preenchimento do tempo restante baseado na prioridade até o limite máximo de cada tema.

**US07: Compensação de Carga Horária aos Sábados - [CANCELADA]**

MOTIVO do CANCELAMENTO: A User Story 09 já utiliza os sábados como uma restrição dos cálculos. Não é uma funcionalidade, mas sim a restrição de uma funcionalidade.

**US08: Proteção Cíclica de Datas de Avaliação - [CANCELADA]**

MOTIVO do CANCELAMENTO: A User Story 06 já estabelece que um conteúdo pode ser uma PROVA. Além disso, a US09 já estabelece que a prova é uma restrição para a geração da planilha.

**US09: Geração de PLANILHA para preenchimento do SIGA**
### HISTÓRIA PRINCIPAL DO PROFESSOR
Como professor, quero visualizar a relação do planejamento final em um formato estruturado (.xlsx), para que as informações de data, tema e ordem sejam facilmente preenchidas no SIGA.
* **Critérios de Aceitação:**
    * Geração de arquivo contendo as colunas: número da aula, data, tema, marcador de prova, dia da semana e identificação da disciplina. (Depende da US06)
    * Numeração sequencial de aulas que avança apenas em dias letivos (ignorando feriados e dias sem aula).
    * Considerar os feriados e/ou dias bloqueados do calendário. (Depende da US01)
    * Considerar sábados. Caso não haja dias suficientes no semestre no semestre, pode-se colocar compensações de aulas aos sábados. Dar preferência aos ÚLTIMOS SÁBADOS, e pode-se utilizar até 5 aulas no sábado. (Depende da US01)
    * Considerar a data de kickoff para criar ciclos de 28 dias por SPRINT. Na SPRINT, deve haver restrição de provas aos dias da 3ª e 4ª semanas. (Depende da US01)
