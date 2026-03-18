# Siga Feliz — Planejamento Semestral Automatizado

Aplicação desktop JavaFX para automação de cronogramas acadêmicos. Distribui aulas ao longo do semestre respeitando feriados, sábados letivos e ciclos de Sprint do Projeto Integrador.

---

## Pré-requisitos

| Ferramenta | Versão mínima | Para quê |
|---|---|---|
| **Java JDK** | 17+ | Compilar e executar a aplicação |
| **Maven** | 3.8+ | Gerenciar dependências e build |
| **Docker + Docker Compose** | 20+ / v2+ | Subir o banco PostgreSQL |

---

## Como rodar

### 1. Clonar o repositório

```bash
git clone https://github.com/Eaglefatec/sigafeliz.git
cd sigafeliz
```

### 2. Subir o banco de dados PostgreSQL

O Docker Compose cria um container PostgreSQL já configurado com o schema do projeto.

```bash
docker-compose up -d
```

Isso irá:
- Baixar a imagem `postgres:16-alpine`
- Criar o banco `sigafeliz` com usuário `sigafeliz` / senha `sigafeliz`
- Executar o script `docker/init.sql` para criar as tabelas
- Disponibilizar o banco na porta **5432**

Para verificar se o banco está rodando:

```bash
docker-compose ps
```

### 3. Compilar e executar a aplicação

```bash
mvn clean javafx:run
```

A aplicação vai abrir uma janela JavaFX e conectar automaticamente ao PostgreSQL em `localhost:5432`.

---

## Uso rápido

1. **Tela inicial** — Escolha "Novo Planejamento" ou "Histórico de Cronogramas"
2. **Painel do Coordenador** — Acesse com `Ctrl+Shift+D` → chave: `eagle`
   - Cadastre semestres (com data de Kickoff do PI)
   - Cadastre feriados, eventos e sábados letivos
3. **Wizard do Professor** — 4 passos: Semestre → Grade → Temas → Gerar
4. **Exportação** — O cronograma é salvo como `.xlsx` com as colunas do SIGA

---

## Configuração avançada

A conexão com o banco pode ser configurada via variáveis de ambiente:

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_HOST` | `localhost` | Host do PostgreSQL |
| `DB_PORT` | `5432` | Porta |
| `DB_NAME` | `sigafeliz` | Nome do banco |
| `DB_USER` | `sigafeliz` | Usuário |
| `DB_PASSWORD` | `sigafeliz` | Senha |

---

## Comandos úteis do Docker

```bash
# Subir o banco
docker-compose up -d

# Ver logs do banco
docker-compose logs db

# Parar o banco
docker-compose down

# Parar e apagar os dados do banco
docker-compose down -v
```

---

## Estrutura do projeto

```
sigafeliz/
├── src/main/java/com/eaglefatec/sigafeliz/
│   ├── App.java                  # Ponto de entrada
│   ├── controller/               # MainController (UI)
│   ├── dao/                      # Acesso a dados (JDBC)
│   ├── engine/                   # Motor de agendamento + Excel
│   └── model/                    # Entidades (Semester, Tema, etc.)
├── src/main/resources/           # CSS + logo
├── docker/init.sql               # Schema do banco
├── docker-compose.yml            # PostgreSQL container
├── pom.xml                       # Dependências Maven
└── docs/                         # Documentação do projeto
```

---

## Tecnologias

- **Java 17** + **JavaFX 21**
- **PostgreSQL 16** (via Docker)
- **Apache POI** (exportação Excel)
- **Maven** (build)

---

*Eagle FATEC — v1.0*
