# Visão Geral e Arquitetura do Projeto: Siga Feliz

## 1. Descrição e Objetivo da Solução

O "Siga Feliz" é uma aplicação desktop voltada para a automação do planejamento semestral de cronogramas acadêmicos com suporte a múltiplos usuários. O objetivo é eliminar o retrabalho operacional e o esforço cognitivo dos professores ao distribuir temas letivos, garantindo a conformidade institucional com a carga exata de 40 ou 80 aulas e respeitando restrições de calendário (feriados, eventos institucionais e ciclos de Sprints). A solução substitui o cruzamento manual de calendários por um algoritmo de alocação inteligente, validação de regras de negócio e isolamento de escopo por docente.

## 2. Requisitos Não Funcionais e Arquitetura

A solução será construída sob as seguintes premissas tecnológicas para garantir robustez, interface responsiva e integridade dos dados compartilhados:
* **Linguagem de Programação:** Java.
* **Interface Gráfica (GUI):** JavaFX, proporcionando uma experiência desktop fluida para a inserção de dados, telas de identificação de usuário e visualização de alertas.
* **Persistência de Dados:** Banco de Dados Relacional Multiusuário (ex: PostgreSQL/MySQL). Para o MVP, a aplicação utilizará uma arquitetura cliente-servidor de 2 camadas, estabelecendo conexão direta (via JDBC) com o banco centralizado para leitura e gravação das configurações institucionais, controle de identidades e geração de cronogramas.

## 3. Atores e Entradas de Dados

### 3.1. Coordenador (Administrador e Parâmetros Institucionais)

Responsável por configurar a base do semestre letivo e gerenciar os acessos, eliminando a necessidade de "dados estáticos" hardcoded:
* Datas de início e término do semestre letivo.
* Definição do pool de sábados letivos institucionais disponíveis para o semestre.
* Cadastro de feriados e eventos (dias sem aula).
* Data de Kickoff (marco zero para o cálculo automático dos ciclos contínuos de Sprint do Projeto Integrador).
* **Gestão de Identidades:** Cadastro de perfis de professores no sistema.
* **Atribuição de Grades:** Criação das disciplinas, definição de suas respectivas grades semanais (dias trabalhados e quantidade de aulas diárias) e vinculação destas grades aos professores cadastrados.

### 3.2. Professor (Usuário Restrito e Planejamento da Disciplina)

Responsável por acessar o sistema e inserir os dados específicos das matérias sob sua responsabilidade:
* **Identificação de Contexto:** Autenticação/seleção de seu perfil de usuário e escolha da disciplina vinculada a ser planejada.
* Carga Horária Total: 40 ou 80 aulas (validação contra a grade atribuída).
* **Definição de Temas (Ementa):**
  * Título do tema.
  * Quantidade mínima e máxima de aulas.
  * Peso de prioridade (Alto, Médio, Baixo).
  * Marcador de Avaliação.

## 4. Regras de Negócio e Processamento

O algoritmo do sistema processará os dados seguindo diretrizes estritas:
1. **Isolamento de Escopo:** O sistema garante que um professor visualize, planeje e gere cronogramas exclusivamente para as disciplinas que lhe foram previamente vinculadas pela coordenação.
2. **Mapeamento de Dias Úteis:** O sistema cruza a grade da disciplina selecionada com o calendário institucional da coordenação, removendo feriados para obter o saldo letivo disponível.
3. **Compensação Institucional (Sábados):** Caso os dias úteis regulares sejam insuficientes para atingir a meta (40/80 aulas), o sistema ativará automaticamente sábados letivos cadastrados. Essa alocação ocorre em ordem cronológica inversa (priorizando os sábados do final do semestre para o início), garantindo a obrigatoriedade da carga.
4. **Distribuição Ponderada:** O sistema aloca primeiro a carga mínima de todos os temas. Em seguida, distribui as aulas restantes com base na prioridade (Alto > Médio > Baixo) até atingir o limite máximo de cada tema ou completar a carga letiva.
5. **Proteção Cíclica de Avaliações:** O sistema projeta ciclos contínuos de 28 dias a partir do Kickoff. Aulas marcadas como "Avaliação" não podem ser alocadas entre o 15º e o 28º dia (3ª e 4ª semana) de nenhum desses ciclos. O sistema emitirá um aviso bloqueante caso a distribuição resulte nesse conflito, exigindo ajuste do professor.

## 5. Entregáveis da Solução

* **Aplicação Desktop (JavaFX):** Interface contendo telas de controle de acesso/login, painéis de administração para coordenadores e telas de planejamento restritas para professores, com validações em tempo real e alertas visuais de inconsistência matemática.
* **Exportação Padronizada (.xlsx):** Geração de arquivo Excel estruturado contendo: Número da Aula, Data, Tema, Marcador de Prova, Dia da Semana, Identificação da Disciplina e Observações (feriados/sprints). Este artefato é o produto final destinado à transcrição eficiente para o sistema acadêmico oficial (SIGA).
