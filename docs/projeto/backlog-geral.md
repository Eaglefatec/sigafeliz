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
| 1º | US01: Definição de Calendário Acadêmico do Semestre | Alta | Baixa | Sprint 3 |
| 2º | US02: Cadastro de Identidade Docente | Alta | Baixa | Sprint 3 |
| 3º | US03: Definição de Grade de Disciplina e Vínculo Docente | Alta | Média | Sprint 3 |
| 4º | US05: Identificação de Usuário e Seleção de Disciplina | Alta | Média | Sprint 2 |
| 5º | US06: Distribuição Automática de Conteúdo | Alta | Alta | Sprint 3 |
| 6º | US04: Estabelecimento do Ciclo de Sprints | Média | Baixa | Sprint 2 |
| 7º | US07: Compensação de Carga Horária aos Sábados | Média | Alta | Sprint 3 |
| 8º | US08: Proteção Cíclica de Datas de Avaliação | Média | Média | Sprint 3 |
| 9º | US09: Compatibilização de Dados para o SIGA | Baixa | Baixa | Sprint  3 |

### 2.2 Coordenador Acadêmico

**US01: Definição de Calendário Acadêmico do Semestre**
### HISTÓRIA PRINCIPAL DO COORDENADOR
Como coordenador, quero fornecer para o professor os parâmetros temporais do semestre regular e o início do Projeto API, para que o planejamento considere simultaneamente os dias úteis e os ciclos contínuos de Sprints.
* **Critérios de Aceitação:**
    * Definição de data de início e término do período letivo.
    * Definição de quais sábados são considerados letivos pelo calendário institucional (para uso em compensações).
    * Identificação de dias específicos como "sem aula" (feriados e eventos).
    * Definição da data exata do Kickoff do Projeto Integrador.
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

**US04: Estabelecimento do Ciclo de Sprints**
Como coordenador, quero que o sistema projete ciclos contínuos de 28 dias a partir da data do Kickoff, para que as restrições de planejamento e datas de Sprint Review sejam mapeadas automaticamente até o fim do semestre.
* **Critérios de Aceitação:**
    * O sistema deve calcular períodos consecutivos de exatamente 28 dias (4 semanas), com o "Dia 1" da primeira Sprint sendo a data do Kickoff.
    * As semanas 3 e 4 de cada ciclo de 28 dias devem ser marcadas internamente no sistema como zonas de restrição para regras de negócio de avaliações.

### 2.3 Professor

**US05: Identificação de Usuário e Seleção de Disciplina**
Como professor, quero me identificar no sistema e visualizar apenas as disciplinas atribuídas a mim, para garantir o isolamento dos dados e focar exclusivamente no meu planejamento.
* **Critérios de Aceitação:**
    * Tela inicial solicitando a identificação do usuário (seja por seleção em lista ou autenticação simples por e-mail/senha, a ser definido na arquitetura final).
    * Após identificação, exibição de uma lista contendo apenas as disciplinas (e suas respectivas grades) vinculadas àquele professor pela coordenação.
    * Restrição sistêmica que impeça a visualização ou edição de disciplinas atribuídas a outros docentes.

**US06: Distribuição Automática de Conteúdo**
### HISTÓRIA PRINCIPAL DO PROFESSOR
Como professor, quero planejar a relação entre temas de aula e dias lecionados com o mínimo de input manual e esforço cognitivo possível, para que a distribuição da matéria ao longo do semestre siga as regras institucionais.
* **Critérios de Aceitação:**
    * A interface deve deixar claro qual disciplina está sendo planejada no momento (selecionada na US05).
    * Cada tema deve ter: título, carga mínima, carga máxima e nível de prioridade.
    * Garantia de que o valor mínimo de todos os temas seja alocado primeiro.
    * Preenchimento do tempo restante baseado na prioridade até o limite máximo de cada tema.

**US07: Compensação de Carga Horária aos Sábados**
Como professor, quero que o cronograma utilize sábados letivos com prioridade para o final do semestre quando os dias de semana forem insuficientes, garantindo o cumprimento exato da carga horária de 40 ou 80 aulas.
* **Critérios de Aceitação:**
    * Identificação automática de déficit de aulas comparando os dias úteis gerados com a meta exigida da disciplina selecionada (40/80).
    * O sistema deve exibir um alerta visual informando a falta de dias regulares e a necessidade de uso de sábados.
    * O algoritmo deve processar os sábados letivos cadastrados pela coordenação no intervalo do semestre.
    * A ativação dos sábados necessários para suprir o déficit deve ocorrer em ordem cronológica inversa (priorizando os sábados mais próximos ao final do semestre letivo).
    * Após a ativação das datas compensatórias, a distribuição dos temas deve seguir a ordem cronológica normal do calendário estruturado.
    * O fechamento do cronograma deve somar exatamente a carga letiva exigida.

**US08: Proteção Cíclica de Datas de Avaliação**
Como professor, quero identificar quais aulas são provas, para que o sistema impeça que elas sejam alocadas nas semanas de entrega de Projeto Integrador, garantindo conformidade com a política institucional de não sobreposição de avaliações.
* **Critérios de Aceitação:**
    * Atribuição de identificador de "Avaliação" aos temas pertinentes.
    * O sistema deve verificar se a data projetada para a avaliação incide entre o 15º e o 28º dia (semanas 3 e 4) de **qualquer** ciclo de Sprint originado pelo Kickoff.
    * Aviso bloqueante na interface caso a distribuição resulte em avaliação alocada dentro dessas semanas restritas em qualquer ponto do semestre, exigindo ajuste por parte do professor.

**US09: Compatibilização de Dados para o SIGA**
Como professor, quero visualizar a relação do planejamento final em um formato estruturado (.xlsx), para que as informações de data, tema e ordem sejam facilmente preenchidas no SIGA.
* **Critérios de Aceitação:**
    * Geração de arquivo contendo as colunas: número da aula, data, tema, marcador de prova, dia da semana e identificação da disciplina.
    * Numeração sequencial de aulas que avança apenas em dias letivos (ignorando feriados e dias sem aula).
