# Visão Geral e Arquitetura do Projeto: Siga Feliz

## 1. Descrição e Objetivo da Solução
O "Siga Feliz" é uma aplicação desktop voltada para a automação do planejamento semestral de cronogramas acadêmicos. O objetivo é eliminar o retrabalho operacional e o esforço cognitivo dos professores ao distribuir temas letivos, garantindo a conformidade institucional com a carga exata de **40 ou 80 aulas** e respeitando restrições de calendário (feriados, eventos institucionais e ciclos de Sprints). A solução substitui o cruzamento manual de calendários por um algoritmo de alocação inteligente e validação de regras de negócio.

## 2. Requisitos Não Funcionais e Arquitetura
A solução será construída sob as seguintes premissas tecnológicas para garantir robustez, interface responsiva e integridade dos dados compartilhados:
* **Linguagem de Programação:** Java.
* **Interface Gráfica (GUI):** JavaFX, proporcionando uma experiência desktop fluida para a inserção de dados e visualização de alertas.
* **Persistência de Dados:** Banco de Dados Relacional Multiusuário (ex: PostgreSQL/MySQL). Para o MVP, a aplicação utilizará uma arquitetura cliente-servidor de 2 camadas, estabelecendo conexão direta (via JDBC) com o banco centralizado para leitura e gravação das configurações institucionais e cronogramas.

## 3. Atores e Entradas de Dados

### 3.1. Coordenador (Parâmetros Institucionais)
Responsável por configurar a base do semestre letivo, eliminando a necessidade de "dados estáticos" hardcoded:
* Datas de início e término do semestre letivo.
* Definição do pool de sábados letivos institucionais disponíveis para o semestre.
* Cadastro de feriados e eventos (dias sem aula).
* Data de Kickoff (marco zero para o cálculo automático dos ciclos contínuos de Sprint do Projeto Integrador).
* Disponibilidade docente: dias da semana trabalhados e capacidade de aulas diária por professor.

### 3.2. Professor (Planejamento da Disciplina)
Responsável por inserir os dados específicos da matéria:
* **Carga Horária Total:** 40 ou 80 aulas.
* **Definição de Temas:** * Título do tema.
  * Quantidade mínima e máxima de aulas.
  * Peso de prioridade (Alto, Médio, Baixo).
  * Marcador de Avaliação.

## 4. Regras de Negócio e Processamento
O algoritmo do sistema processará os dados seguindo diretrizes estritas:
1. **Mapeamento de Dias Úteis:** O sistema cruza a grade do professor com o calendário da coordenação, removendo feriados para obter o saldo letivo.
2. **Compensação Institucional (Sábados):** Caso os dias úteis regulares sejam insuficientes para atingir a meta (40/80 aulas), o sistema ativará automaticamente sábados letivos cadastrados. Essa alocação ocorre em ordem cronológica inversa (priorizando os sábados do final do semestre para o início), garantindo a obrigatoriedade da carga.
3. **Distribuição Ponderada:** O sistema aloca primeiro a carga mínima de todos os temas. Em seguida, distribui as aulas restantes com base na prioridade (Alto > Médio > Baixo) até atingir o limite máximo de cada tema ou completar a carga letiva.
4. **Proteção Cíclica de Avaliações:** O sistema projeta ciclos contínuos de 28 dias a partir do Kickoff. Aulas marcadas como "Avaliação" não podem ser alocadas entre o 15º e o 28º dia (3ª e 4ª semana) de nenhum desses ciclos. O sistema emitirá um **aviso bloqueante** caso a distribuição resulte nesse conflito, exigindo ajuste do professor.

## 5. Entregáveis da Solução
* **Aplicação Desktop (JavaFX):** Interface contendo telas de configuração para coordenadores e telas de planejamento para professores, com validações em tempo real e alertas visuais de inconsistência matemática.
* **Exportação Padronizada (.xlsx):** Geração de arquivo Excel estruturado contendo: Número da Aula, Data, Tema, Marcador de Prova, Dia da Semana e Observações (feriados/sprints). Este artefato é o produto final destinado à transcrição eficiente para o sistema acadêmico oficial (SIGA).