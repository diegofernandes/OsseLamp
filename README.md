# OsseLamp

App Android para controlar uma lâmpada RGB: ligar/desligar, escolher a cor,
ajustar o brilho e ativar modos de efeito ("Fire" e "Rainbow").

> **Status:** em desenvolvimento. A interface está pronta, mas ainda não existe
> a camada de comunicação com a lâmpada — os controles não enviam comandos.

## Funcionalidades

| Controle | Estado |
|---|---|
| Seletor de cor circular (`ColorCircle`) | ✅ funcionando (arrastar no anel escolhe a cor, tocar no centro confirma) |
| Botão liga/desliga | 🚧 só interface |
| Barra de brilho | 🚧 só interface |
| Modos Fire / Rainbow | 🚧 só interface |
| Menu "Connect" | 🚧 só interface |

## Estrutura

```
app/src/main/java/br/com/diegofernandes/osselamp/
├── MainActivity.java              # tela principal
└── widget/
    ├── ColorCircle.java           # view customizada do seletor de cor
    ├── ColorMath.java             # cálculo de ângulo → cor (sem dependências Android)
    └── OnColorChangedListener.java
app/src/test/                      # testes unitários (JUnit)
```

## Como compilar

O projeto usa um toolchain antigo:

- Android Gradle Plugin 1.2.3 / Gradle 2.2.1 (via `gradlew`)
- `compileSdkVersion` 22, `buildToolsVersion` 22.0.0, `minSdkVersion` 18
- Support Library `appcompat-v7:22.1.1`
- JDK 7 ou 8

```sh
./gradlew assembleDebug   # gera o APK
./gradlew test            # roda os testes unitários
```

Para abrir no Android Studio, use *Open* e selecione a pasta do projeto; os
arquivos `.idea/` e `*.iml` são gerados a partir do Gradle.

Versões recentes do Android Studio/JDK não suportam esse toolchain — será
necessário migrar para um Gradle/AGP atuais e para AndroidX.

## Próximos passos

- Implementar a comunicação com a lâmpada e ligá-la aos controles da tela.
- Modernizar o build (Gradle/AGP, AndroidX, `targetSdkVersion` atual).
