# Descrição geral: Siga Feliz

## 1. Objetivo do Projeto
A solução consiste em um programa de computador para automatizar o planejamento semestral de cronogramas acadêmicos. O sistema distribuirá automaticamente as aulas pelos dias letivos, garantindo a carga exata de **40 ou 80 aulas** por semestre. O programa mapeará os temas, aplicará restrições de calendário (feriados e eventos) e exigirá a inserção mínima de dados por parte do professor.

## 2. Dores do Cliente
* **Conformidade Institucional:** Necessidade de cumprir prazos de entrega do cronograma no início do semestre letivo.
* **Ineficiência da Interface Atual:** O sistema atual exige o cruzamento manual de formulários e calendários, pois não exibe as datas exatas durante o preenchimento, apenas a sequência numérica das aulas.
* **Retrabalho Operacional:** Esforço cognitivo e manual repetitivo para distribuir os temas ao longo dos dias a cada novo semestre.
* **Complexidade de Validação:** Dificuldade em conciliar a carga horária exigida com feriados e regras institucionais (ex: semanas de avaliações e sprints).

## 3. Entradas de Dados (Inputs)

### 3.1. Dados Inseridos pelo Usuário
* **Carga Horária Total:** 40 ou 80 aulas.
* **Grade Horária Semanal:** Dias da semana e capacidade diária de aulas do professor.
* **Definição de Temas:** * Título do tema.
  * Quantidade mínima e máxima de aulas para aquele tema.
  * Peso de prioridade (Alto, Médio, Baixo).
  * Flag de Avaliação: Identifica se o tema é uma prova/avaliação.

### 3.2. Dados Estáticos (Base do Sistema)
O sistema trará os seguintes dados embutidos, sem necessidade de digitação do professor:
* Datas de início e término do semestre letivo.
* Feriados nacionais, locais e eventos institucionais (dias sem aula).
* Datas de Kickoff para cálculo das semanas de Sprint.
* Dias bloqueados por regras do projeto (ex: Sprint Reviews).

## 4. Regras de Negócio e Restrições
* **Hierarquia de Dados:** Matéria -> Aulas -> Temas.
* **Obrigatoriedade de Carga:** O cronograma deve fechar exatamente em 40 ou 80 aulas.
* **Restrição de Provas:** Aulas com a opção "Avaliação" marcada não podem ocorrer na 3ª e 4ª semana após o Kickoff da Sprint.
* **Pré-validação de Capacidade:** O programa não gerará o cronograma se houver inconsistências matemáticas, como: falta de dias úteis no calendário para alocar as aulas, ou parâmetros de temas incompatíveis com o total exigido. **A CONFIRMAR**

## 5. Processamento e Lógica de Distribuição
O sistema realizará a distribuição seguindo estes passos:
1. **Geração de Dias Úteis:** Cruzamento da grade do professor com o calendário letivo, removendo feriados e bloqueios, para listar os dias reais de aula.
2. **Alocação Mínima:** Reserva da quantidade mínima de aulas definida para cada tema.
3. **Distribuição de Sobras:** Preenchimento das aulas restantes utilizando os temas com maior "Peso de Prioridade", até que alcancem sua quantidade máxima ou até que o semestre atinja a carga total (40/80).
4. **Associação de Datas:** Atribuição de cada aula da lista gerada aos dias úteis disponíveis no calendário, cronologicamente, de acordo com a capacidade de aulas em cada dia.
5. **Realocação de Provas:** Caso uma avaliação caia na 3ª semana da Sprint (restrição da regra 4), o sistema `trocará a data dessa prova pela data da próxima aula teórica disponível` ou `irá alertar a inconsistência` **A NEGOCIAR**.

## 6. Entregáveis
* **Interface de Preenchimento:** Programa com interface de uso simples para a inserção dos dados mínimos pelo professor.
* **Alertas Visuais:** Avisos claros na tela impedindo o avanço caso os dados inseridos não fechem a matemática do semestre (Ex: "O calendário possui apenas 76 dias úteis para a sua grade. Não é possível alocar 80 aulas.").
* **Exportação para Excel:** Geração de um arquivo `.xlsx` estruturado para facilitar a cópia para o sistema Siga, contendo: Data, Hora, Número da Aula, Tema e Quantidade de aulas no dia.

---

### Dúvidas Críticas de Negócio

* **Resolução de Falta de Dias:** Caso o cruzamento do calendário com a grade do professor resulte em dias insuficientes para atingir as 40/80 aulas, existe um protocolo institucional de reposição a ser embutido no sistema `(inserção em sábados)`, ou o software apenas bloqueia a geração?
* **Lógica de Priorização:** Definir o modelo de distribuição de temas por peso "Alto" em relação aos de peso "Médio" ou "Baixo".
* **Casos de "Ponta" de Preenchimento:** Considerando `min` a soma mínima de aulas de todos os temas, `max` a soma máxima, e `x` a carga total (40 ou 80):
  * Se `min + max < x`: O que o sistema deve fazer com as aulas que sobrarão sem tema `(preencher o resto como "Fechamento?)`.
  * Se a soma máxima `max > x` e `min < x`: Definir o modelo de distribuição das aulas máximas por prioridade do tema.
* **Estrutura do Excel Final:** Quais são as colunas exatas úteis para o preenchimento no Siga que o Excel gerado deve conter para auxiliar no cruzamento das informações?