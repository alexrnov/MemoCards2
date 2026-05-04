package alexrnov.animememo.cards

import alexrnov.enginegl.CardVertices
import alexrnov.enginegl.ObjectSize
import alexrnov.animememo.enginegl.Object3D

data class Card(
	val id: Int,
	val front: Object3D,
	val back: Object3D,
	val frontPath: String
) {
	fun position(x: Float, y: Float, z: Float, f: Float) {
		front.position(x, y, z, f)
		back.position(x, y, z, f)
	}

	fun draw(viewMatrix: FloatArray, projectionMatrix: FloatArray) {
		front.defineView(viewMatrix, projectionMatrix)
		front.draw()
		back.defineView(viewMatrix, projectionMatrix)
		back.draw()
	}

	fun defineView(viewMatrix: FloatArray, projectionMatrix: FloatArray) {
		front.defineView(viewMatrix, projectionMatrix)
		back.defineView(viewMatrix, projectionMatrix)
	}

	fun isOpen(): Boolean = front.isFront

	fun setRotationProcess(rotate: Boolean, rotateFunction: (() -> Unit)? = null) {
		front.isRotationProcess = rotate
		back.isRotationProcess = rotate

		front.setRotateFunction(rotateFunction)
	}

	fun isRotationProcess(): Boolean = front.isRotationProcess

	fun rotate(delta: Float, k: Float) {
		front.rotate(delta, 0.0f, -0.5f, 0.0f, k)
		back.rotate(delta, 0.0f, -0.5f, 0.0f, k)
	}

	fun openCard() {
		front.rotate180(0.0f, -0.5f, 0.0f)
		back.rotate180(0.0f, -0.5f, 0.0f)
	}

	fun getSize(projectionMatrix: FloatArray, widthScreen: Int, heightScreen: Int, scale: Float, modelX: Float, modelY: Float, modelZ: Float): ObjectSize {
		return front.getSize(projectionMatrix, widthScreen, heightScreen, scale, modelX, modelY, modelZ)
	}

	fun getVertices(projectionMatrix: FloatArray, widthScreen: Int, heightScreen: Int, scale: Float, modelX: Float, modelY: Float, modelZ: Float): CardVertices {
		return front.getVertices(projectionMatrix, widthScreen, heightScreen, scale, modelX, modelY, modelZ)
	}
}