# Cinema

Aplicacion Android para explorar series de television mediante la API publica de [TVMaze](https://www.tvmaze.com/api). La app muestra contenido destacado y secciones de series, permite consultar el detalle de cada programa y ofrece informacion como puntuacion, generos, reparto, sitio oficial y un reproductor de video de muestra. Tambien incluye un selector de tema claro, oscuro o dependiente del sistema.

## Autor

- **Elhellby**

## Instrucciones de uso

1. Instala la aplicacion en un dispositivo Android o emulador con Android 8.0 (API 26) o superior.
2. Abre **Cinema**.
3. Desde la pantalla principal, desplaza el contenido para consultar las series disponibles.
4. Toca una serie para abrir su ficha y consultar su informacion, reparto, sitio oficial y video de muestra.
5. Usa el icono de ajustes para elegir el tema **Sistema**, **Claro** u **Oscuro**.
6. Si la carga falla, pulsa **Reintentar**. La pantalla principal tambien admite el gesto de deslizar hacia abajo para actualizar el catalogo.

La aplicacion necesita conexion a Internet para consultar TVMaze y cargar las imagenes. El video mostrado en el detalle es un recurso de demostracion remoto.

## Compilacion y ejecucion

### Requisitos

- Android Studio con soporte para Android Gradle Plugin 9.3.3.
- JDK 17.
- Android SDK Platform 37 y Build Tools compatibles.
- Un dispositivo o emulador con API 26 o superior.
- Acceso a Internet durante la compilacion inicial y el uso de la aplicacion.

El proyecto usa Gradle Wrapper, por lo que no es necesario instalar Gradle manualmente.

### Compilar

Desde la raiz del proyecto:

```bash
./gradlew test
./gradlew assembleDevDebug
```

- `test` ejecuta las pruebas unitarias.
- `assembleDevDebug` genera el APK de desarrollo en `app/build/outputs/apk/dev/debug/`.

Para generar la variante de produccion:

```bash
./gradlew assembleProdDebug
./gradlew assembleProdRelease
```

La variante `dev` habilita logging y usa el identificador de aplicacion `com.hellby.cinema.dev`. La variante `prod` deshabilita el logging y usa `com.hellby.cinema`.

### Ejecutar en un dispositivo o emulador

Con un dispositivo conectado o un emulador iniciado, instala y ejecuta la variante de desarrollo con:

```bash
./gradlew installDevDebug
```

Tambien puedes abrir el proyecto en Android Studio, seleccionar la configuracion `app` y pulsar **Run**.

### Configuracion de red

La aplicacion usa `https://api.tvmaze.com/` como URL base y declara el permiso `INTERNET` en el manifiesto. No se requiere una clave API.

## Tecnologias principales

- Kotlin y Jetpack Compose
- Material 3
- Hilt para inyeccion de dependencias
- Retrofit, Kotlin Serialization y OkHttp para red
- Coil 3 para imagenes
- AndroidX Media3 para video
- DataStore para preferencias
- Navigation Compose para navegacion

## Pruebas

Las pruebas unitarias cubren el servicio remoto, mapeo de datos, repositorio de catalogo, manejo de errores y los ViewModel de inicio y detalle:

```bash
./gradlew test
```

## Fuente de datos

Los datos de series proceden de [TVMaze](https://www.tvmaze.com/). Consulta sus [terminos de uso](https://www.tvmaze.com/api#terms) antes de redistribuir o publicar la aplicacion.
