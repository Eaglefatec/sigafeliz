# 📌 Guia de Padronização de Commits - Eagle

Para manter o histórico do nosso projeto organizado e compreensível, utilizamos o padrão Conventional Commits. Este guia define a estrutura que todos os membros da equipe devem seguir ao realizar envios para o repositório.

## 🏗️ Estrutura do Commit

Cada mensagem de commit deve seguir este formato:

**tipo(escopo): descrição curta** <br>
**[corpo opcional]**

### Tipo do Commit
O tipo indica a natureza da alteração. Os principais tipos que utilizaremos são:

* **feat:** Uma nova funcionalidade para o usuário.
* **fix:** Correção de um erro (bug).
* **docs:** Alterações apenas na documentação (ex: README, comentários).
* **style:** Mudanças que não afetam o código (espaços, formatação, ponto e vírgula).
* **refactor:** Alteração no código que não corrige erro nem adiciona funcionalidade.
* **perf:** Mudança de código focada em melhorar o desempenho. 
* **test:** Adição ou correção de testes existentes. 
* **chore:** Atualizações de tarefas de build, pacotes ou ferramentas auxiliares.

### Escopo
Você pode incluir o escopo entre parênteses para especificar a parte do código que foi alterada. 

Exemplo: **feat(login):** ou **fix(api):**

### Descrição
Uma frase curta e clara resumindo a mudança.

* Use o imperativo (ex: "adiciona" em vez de "adicionado").
* Use somente letras minúsculas.
* Não coloque ponto final.

## ✅ Exemplos

* **Commit Simples (Funcionalidade)** <br>
feat: adiciona sistema de autenticação via Google

* **Commit com Escopo (Correção)** <br>
fix(database): corrige erro de conexão em ambiente de produção

* **Commit com Corpo**<br>
refactor: simplifica lógica de cálculo de frete <br>
O algoritmo anterior tinha complexidade O(n^2). Esta versão utiliza um hash map para reduzir o tempo de execução para O(n).

* **Breaking Changes (Mudanças Críticas)** <br>
Se uma alteração quebrar a compatibilidade com versões anteriores, adicione um sinal de exclamação após o tipo ou inclua BREAKING CHANGE: no rodapé.

    **Exemplo:** feat!: altera o endpoint principal da API v1 para v2

## 💡 Dicas Importantes

* **Commits Atômicos:** Tente realizar commits pequenos que resolvam apenas um problema ou adicionem uma única funcionalidade.

* **Frequência:** Realize commits frequentemente para evitar grandes conflitos de merge.
