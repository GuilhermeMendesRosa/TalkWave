## 🌊 TalkWave 🌊

O TalkWave é um aplicativo de mensagens desenvolvido em Java que permite comunicação em tempo real por meio de sockets em uma rede local.

Este projeto foi desenvolvido como parte do curso de TADS da UDESC, na disciplina de Dispostivos Móveis 2024/1.

## Tecnologias Utilizadas
- ☕ Java 17
- 🌐 Sockets
- 🧵 Threads

## Como Utilizar

### Pré-requisitos
- Java 17

### Execução
1. Executar o servidor:
```bash
cd ./talk-wave-server
mvn clean package
java -jar target/talk-wave-client-1.0-SNAPSHOT.jar
```

2. Executar o cliente:
```bash
cd ./talk-wave-client
mvn clean package
java -jar target/talk-wave-client-1.0-SNAPSHOT.jar
```


## Funcionalidades
- [x] Envio de mensagens:
  - [x] `/send message <destino> <message>`
  - [x] `/send message to:<destino1>,<destino2> <message>`
- [x] Envio de arquivos
  - [x] `/send file <destino> <path>`

- [x] Listagem de usuários
  - [x] `/users`
- [x] Sair
  - [x] `/sair`
- [x] Última atividade de cada usuário

- [x] Administrador: Banir, listar usuários, auditar mensagens
  - [x] `#audit`
- [x] Banimento de usuários inativos (com aviso prévio)

### Repositórios Git:
- [Servidor](https://github.com/GuilhermeMendesRosa/TalkWave)
- [Cliente](https://github.com/jpdev01/talk-wave-client)