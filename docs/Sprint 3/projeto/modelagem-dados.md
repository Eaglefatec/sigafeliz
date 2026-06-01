# Modelagem de Dados: Siga Feliz

> **Fonte de verdade:** `backlog-geral.md`
> O Excel gerado (US09) **não é salvo no banco** — é gerado na hora e descartado. O cruzamento entre Disciplina e Semestre ocorre apenas em tempo de execução na interface.

---

## 1. Entidades de Calendário

As tabelas abaixo representam os parâmetros temporais institucionais, cadastrados pela coordenação, e não possuem vínculo direto no banco de dados com professores ou matérias.

### `semestre`
Parâmetros do período letivo (US01).

| Coluna | Tipo | Observação |
|---|---|---|
| `nome` | VARCHAR(20) | **Chave primária** (ex: `"2025.1"`) |
| `data_inicio` | DATE | Data de início das aulas |
| `data_fim` | DATE | Data de encerramento das aulas |
| `data_kickoff` | DATE | Marco zero do Projeto Integrador (US04) |

### `sabado_letivo`
Sábados disponíveis para compensação de carga horária (US01, US07).

| Coluna | Tipo | Observação |
|---|---|---|
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 1)** — FK → `semestre.nome` |
| `data` | DATE | **Chave primária (parte 2)** |

### `dia_restrito`
Feriados e eventos sem aula (US01).

| Coluna | Tipo | Observação |
|---|---|---|
| `semestre_nome` | VARCHAR(20) | **Chave primária (parte 1)** — FK → `semestre.nome` |
| `data` | DATE | **Chave primária (parte 2)** |
| `descricao` | VARCHAR(255) | Opcional (ex: `"Feriado Nacional"`) |

---

## 2. Entidades Acadêmicas

As tabelas abaixo representam a estrutura de aulas e ementas, que são reaproveitadas a cada novo período letivo.

### `professor`
Cadastro dos docentes (US02).

| Coluna | Tipo | Observação |
|---|---|---|
| `nome` | VARCHAR(255) | Nome do docente |
| `email` | VARCHAR(255) | **Chave primária** |

### `disciplina`
A matéria em si, vinculada ao docente responsável (US03).

| Coluna | Tipo | Observação |
|---|---|---|
| `nome` | VARCHAR(255) | **Chave primária** (ex: `"Banco de Dados 1"`) |
| `professor_email` | VARCHAR(255) | FK → `professor.email` |
| `carga_horaria_total` | INT | Deve ser `40` ou `80` |

### `aulas_por_dia`
Grade semanal fixa da disciplina, definindo quantas aulas ocorrem em cada dia da semana (US03).

| Coluna | Tipo | Observação |
|---|---|---|
| `disciplina_nome` | VARCHAR(255) | **Chave primária (parte 1)** — FK → `disciplina.nome` |
| `dia_semana` | INT | **Chave primária (parte 2)** — `1`=Seg, `2`=Ter, ..., `6`=Sáb |
| `quantidade_aulas` | INT | Ex: `2` (duas aulas por dia nesse dia da semana) |

### `tema`
A ementa da disciplina, inserida pelo professor (US06, US08).

| Coluna | Tipo | Observação |
|---|---|---|
| `disciplina_nome` | VARCHAR(255) | **Chave primária (parte 1)** — FK → `disciplina.nome` |
| `titulo` | VARCHAR(255) | **Chave primária (parte 2)** |
| `carga_minima` | INT | Mínimo de aulas para este tema |
| `carga_maxima` | INT | Máximo de aulas para este tema |
| `prioridade` | VARCHAR(5) | `'ALTA'`, `'MEDIA'` ou `'BAIXA'` |
| `e_avaliacao` | BOOLEAN | `TRUE` se for uma prova (US08) |
| `ordem` | INT | Posição do tema na ementa (1º, 2º...) |

---

## 3. Relacionamentos

```text
semestre   ──< sabado_letivo    (1 semestre tem vários sábados)
semestre   ──< dia_restrito     (1 semestre tem vários feriados/eventos)
professor  ──< disciplina       (1 professor é responsável por várias disciplinas)
disciplina ──< aulas_por_dia    (1 disciplina tem aulas em vários dias da semana)
disciplina ──< tema             (1 disciplina tem vários temas em sua ementa)
```

## 4. O que não é persistido

| Item | Por quê |
| :--- | :--- |
| Ciclos de Sprint (28 dias) | Calculado em tempo de execução a partir de `semestre.data_kickoff`. |
| Saldo de dias úteis | Calculado dinamicamente cruzando a grade (`aulas_por_dia`), o calendário (`semestre`) e os feriados (`dia_restrito`). |
| Arquivo Excel (.xlsx) | Gerado na hora e salvo localmente no computador do usuário (USO9). |
| Vínculo Disciplina x Semestre | Ocorre apenas na RAM da aplicação (View/Controller) quando o professor seleciona um calendário para aplicar sua ementa e gerar o Excel. |
