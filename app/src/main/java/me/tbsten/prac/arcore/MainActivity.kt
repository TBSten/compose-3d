package me.tbsten.prac.arcore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.sceneview.Scene
import io.github.sceneview.math.Position
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import me.tbsten.prac.arcore.ui.theme.PracArCoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracArCoreTheme {
                Bear3D("fatBear.glb")
            }
        }
    }
}

/**
 * ref: https://github.com/SceneView/sceneview-android?tab=readme-ov-file#3d-scene-filament
 */
@Composable
fun Bear3D(
    assetFileLocation: String,
    modifier: Modifier = Modifier,
) {
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine)

    val cameraNode = rememberCameraNode(engine).apply {
        position = Position(z = -4f)
    }
    val centerNode = rememberNode(engine)
        .addChildNode(cameraNode)

    Scene(
        modifier = modifier,
        engine = engine,
        modelLoader = modelLoader,
        cameraNode = cameraNode,
        childNodes = listOf(
            centerNode,
            /*
            Cannot inline bytecode built with JVM target 17 into bytecode that is being built with JVM target 1.8. Please specify proper '-jvm-target' option
            というエラーが表示されるかもしれないが、IDEのバグ？っぽく ビルドは通るので無視して良さそう。
            */
            rememberNode {
                ModelNode(
                    modelInstance = modelLoader.createModelInstance(
                        assetFileLocation = assetFileLocation,
                    ),
                    scaleToUnits = 1.0f
                )
            },
        ),
        // 背景が透明になる
        isOpaque = false,
        onFrame = {
            cameraNode.lookAt(centerNode)
        },
    )
}
