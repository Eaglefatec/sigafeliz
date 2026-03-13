# Descrição geral: Siga Feliz

## 1. Objetivo do Projeto
A solução consiste em uma aplicação desktop desenvolvida em Java/JavaFX para automatizar e otimizar o planejamento semestral de cronogramas acadêmicos. O sistema garantirá a distribuição cronológica exata de **40 ou 80 aulas** por semestre, mapeando temas, respeitando feriados institucionais e restrições pedagógicas, exigindo a entrada mínima de dados pelo professor.

## 2. Dores do Cliente
* **Conformidade Institucional:** Necessidade de cumprir prazos do centro acadêmico no início do semestre, automatizando a geração do artefato final.
* **Ineficiência da Interface Atual:** O sistema em uso não exibe datas exatas no momento do preenchimento, apenas a sequência numérica das aulas, exigindo o cruzamento manual de formulários e calendários.
* **Retrabalho Operacional:** Eliminação do esforço cognitivo repetitivo de distribuição das aulas a cada semestre letivo.
* **Complexidade de Validação:** Dificuldade em conciliar manualmente a carga horária necessária com feriados, eventos institucionais e restrições de Sprint (kickoffs e reviews).

## 3. Entradas de Dados (Inputs)

### 3.1. Dados Dinâmicos (Input do Usuário)
* **Carga Horária Total:** Seleção obrigatória do total letivo do semestre (40 ou 80 aulas).
* **Grade Horária Semanal:** Mapeamento dos dias da semana em que o professor ministra aulas e a capacidade diária de aulas.
* **Definição de Temas:** * Título do tema.
  * Carga horária mínima e máxima em número de aulas.
  * Peso de prioridade do tema (Alto=3, Médio=2, Baixo=1).
  * Flag de Avaliação: Identifica se o tema compreende aplicação de prova.

### 3.2. Dados Estáticos/Constantes (Base do Sistema)
O sistema trará embarcado os dados de base do calendário letivo (definidos no escopo do Projeto Integrador), subtraindo a necessidade de inserção manual:
* Datas de início e término do semestre.
* Feriados nacionais, locais e eventos institucionais (dias não letivos).
* Datas de Kickoff para rastreamento e cálculo de semanas de Sprint.
* Dias bloqueados inerentes às regras do projeto (ex: Sprint Reviews, Edge Cases).

## 4. Regras de Negócio básicas e Restrições
* **Hierarquia de Dados:** A estrutura segue a dependência relacional: Grade Curricular -> Aulas -> Temas.
* **Obrigatoriedade de Carga:** O cronograma gerado deve totalizar, obrigatoriamente, 40 ou 80 aulas.
* **Restrição de Avaliações:** O sistema calculará automaticamente a **3ª semana após a data de Kickoff** da Sprint. Aulas com a flag de "Avaliação" estão proibidas de serem alocadas neste intervalo.
* **Pré-validação Matemática:** O algoritmo bloqueará a execução se a capacidade do calendário for menor que a carga horária exigida, ou se a soma das restrições (mínimas ou máximas) dos temas tornar a equação insolúvel.

## 5. Processamento e Algoritmo de Distribuição
O núcleo de processamento funcionará de forma determinística:
1. **Filtro de Calendário:** Geração de um vetor temporal de dias úteis cruzando a Grade Horária Semanal com o Calendário Letivo, subtraindo feriados e bloqueios.
2. **Alocação Base:** Iteração sobre a lista de temas, reservando o número mínimo de aulas estabelecido para cada um.
3. **Distribuição por Peso (Priorização em caso de sobra):** Caso o número mínimo seja integralmente distribuído, as aulas restantes priorizando os temas com maior "Peso de Prioridade", iterando e adicionando aulas até o limite máximo de cada tema ou até atingir a Carga Horária Total.
4. **Indexação Ordinal e Temporal:** Associação da lista linear de aulas gerada aos dias úteis (Vetor do Passo 1). A Aula 1 recebe a primeira data disponível, e assim sucessivamente.
5. **Resolução de Colisões:** Se uma aula mapeada com flag "Avaliação" colidir com a RN03 (3ª semana da Sprint), o sistema realizará uma troca de posição (*swap*) cronológica com a próxima aula teórica disponível na fila.

## 6. Entregáveis
* **Interface de Preenchimento:** Software executável (JavaFX) com formulários otimizados para entrada dos dados dinâmicos do professor.
* **Tratamento de Exceções Visual:** Alertas impeditivos na interface caso os inputs do professor violem as regras matemáticas do calendário (Ex: "Aviso: O calendário possui apenas 76 aulas disponíveis para a sua grade. Não é possível gerar um cronograma de 80 aulas.").
* **Exportação (Arquivo Excel):** Geração de um documento `.xlsx` contendo a listagem final, relacionando: Data e Hora, Número Ordinal da Aula, Tema da Aula e Quantidade de aulas no dia.


### Pontos para Investigação Adicional

* **Calendário possível:** O calendário disponibilizado sempre permitirá a inserção do número mínimo de 80/40 aulas? Em outras palavras, é possível que o calendário academico bloqueie os dias disponíveis da grade do professor de tal forma que eles não somem 80/40 aulas?

* **Lógica de Priorização:** Definir o peso matemático para temas que devem tender ao limite máximo de aulas permitidas em detrimento de outros.