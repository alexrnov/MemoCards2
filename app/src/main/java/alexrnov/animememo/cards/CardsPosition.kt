package alexrnov.animememo.cards

fun setComposition(cards: Map<Int, Card>, portrait: Boolean) {
	when (cards.size) {
		6 -> setCompositionFor6Cards(cards, portrait)
		12 -> setCompositionFor12Cards(cards, portrait)
		16 -> setCompositionFor16Cards(cards, portrait)
		20 -> setCompositionFor20Cards(cards, portrait)
		30 -> setCompositionFor30Cards(cards, portrait)
	}
}

fun setCompositionForLargeCards(cards: Map<Int, Card>, portrait: Boolean) {
	val z = if (portrait) 2.0f else 1.0f
	cards.forEach { card ->
		card.value.position(0f, 0f, z, 0.0f)
	}
}

private fun setCompositionFor6Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-1.05f, 0.92f, 0.0f, 0.0f)
		cards.getValue(1).position(0f, 0.92f, 0f, 0.0f)
		cards.getValue(2).position(1.05f, 0.92f, 0.0f, 0.0f)

		// второй ряд
		cards.getValue(3).position(-1.05f, -0.92f, 0.0f, 0.0f)
		cards.getValue(4).position(0f, -0.92f, 0.0f, 0.0f)
		cards.getValue(5).position(1.05f, -0.92f, 0.0f, 0.0f)
	} else {
		// один ряд
		cards.getValue(0).position(-2.65f, 0.0f, 0.0f, 0.0f)
		cards.getValue(1).position(-1.59f, 0.0f, 0f, 0.0f)
		cards.getValue(2).position(-0.53f, 0.0f, 0.0f, 0.0f)
		cards.getValue(3).position(0.53f, 0.0f, 0.0f, 0.0f)
		cards.getValue(4).position(1.59f, 0.0f, 0f, 0.0f)
		cards.getValue(5).position(2.65f, 0.0f, 0.0f, 0.0f)
	}
}

private fun setCompositionFor12Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-1.05f, 2.76f, 0.0f, 0.0f)
		cards.getValue(1).position(0f, 2.76f, 0f, 0.0f)
		cards.getValue(2).position(1.05f, 2.76f, 0.0f, 0.0f)

		// второй ряд
		cards.getValue(3).position(-1.05f, 0.92f, 0.0f, 0.0f)
		cards.getValue(4).position(0f, 0.92f, 0f, 0.0f)
		cards.getValue(5).position(1.05f, 0.92f, 0.0f, 0.0f)

		// третий ряд
		cards.getValue(6).position(-1.05f, -0.92f, 0.0f, 0.0f)
		cards.getValue(7).position(0f, -0.92f, 0.0f, 0.0f)
		cards.getValue(8).position(1.05f, -0.92f, 0.0f, 0.0f)

		// четвертый ряд
		cards.getValue(9).position(-1.05f, -2.76f, 0.0f, 0.0f)
		cards.getValue(10).position(0f, -2.76f, 0f, 0.0f)
		cards.getValue(11).position(1.05f, -2.76f, 0.0f, 0.0f)
	} else {
		// первый ряд
		cards.getValue(0).position(-2.65f, 0.92f, 0.0f, 0.0f)
		cards.getValue(1).position(-1.59f, 0.92f, 0f, 0.0f)
		cards.getValue(2).position(-0.53f, 0.92f, 0.0f, 0.0f)
		cards.getValue(3).position(0.53f, 0.92f, 0.0f, 0.0f)
		cards.getValue(4).position(1.59f, 0.92f, 0f, 0.0f)
		cards.getValue(5).position(2.65f, 0.92f, 0.0f, 0.0f)

		// второй ряд
		cards.getValue(6).position(-2.65f, -0.92f, 0.0f, 0.0f)
		cards.getValue(7).position(-1.59f, -0.92f, 0f, 0.0f)
		cards.getValue(8).position(-0.53f, -0.92f, 0.0f, 0.0f)
		cards.getValue(9).position(0.53f, -0.92f, 0.0f, 0.0f)
		cards.getValue(10).position(1.59f, -0.92f, 0f, 0.0f)
		cards.getValue(11).position(2.65f, -0.92f, 0.0f, 0.0f)
	}
}

private fun setCompositionFor16Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-1.56f, 2.76f, 0.0f, 0.0f)
		cards.getValue(1).position(-0.52f, 2.76f, 0f, 0.0f)
		cards.getValue(2).position(0.52f, 2.76f, 0.0f, 0.0f)
		cards.getValue(3).position(1.56f, 2.76f, 0.0f, 0.0f)

		// второй ряд
		cards.getValue(4).position(-1.56f, 0.92f, 0f, 0.0f)
		cards.getValue(5).position(-0.52f, 0.92f, 0.0f, 0.0f)
		cards.getValue(6).position(0.52f, 0.92f, 0.0f, 0.0f)
		cards.getValue(7).position(1.56f, 0.92f, 0.0f, 0.0f)

		// третий ряд
		cards.getValue(8).position(-1.56f, -0.92f, 0.0f, 0.0f)
		cards.getValue(9).position(-0.52f, -0.92f, 0.0f, 0.0f)
		cards.getValue(10).position(0.52f, -0.92f, 0f, 0.0f)
		cards.getValue(11).position(1.56f, -0.92f, 0.0f, 0.0f)

		// четвертый ряд
		cards.getValue(12).position(-1.56f, -2.76f, 0.0f, 0.0f)
		cards.getValue(13).position(-0.52f, -2.76f, 0f, 0.0f)
		cards.getValue(14).position(0.52f, -2.76f, 0.0f, 0.0f)
		cards.getValue(15).position(1.56f, -2.76f, 0.0f, 0.0f)
	} else {
		// первый ряд
		cards.getValue(0).position(-3.71f, 0.92f, 0.0f, 0.0f)
		cards.getValue(1).position(-2.65f, 0.92f, 0.0f, 0.0f)
		cards.getValue(2).position(-1.59f, 0.92f, 0f, 0.0f)
		cards.getValue(3).position(-0.53f, 0.92f, 0.0f, 0.0f)
		cards.getValue(4).position(0.53f, 0.92f, 0.0f, 0.0f)
		cards.getValue(5).position(1.59f, 0.92f, 0f, 0.0f)
		cards.getValue(6).position(2.65f, 0.92f, 0.0f, 0.0f)
		cards.getValue(7).position(3.71f, 0.92f, 0f, 0.0f)

		// второй ряд
		cards.getValue(8).position(-3.71f, -0.92f, 0.0f, 0.0f)
		cards.getValue(9).position(-2.65f, -0.92f, 0.0f, 0.0f)
		cards.getValue(10).position(-1.59f, -0.92f, 0f, 0.0f)
		cards.getValue(11).position(-0.53f, -0.92f, 0.0f, 0.0f)
		cards.getValue(12).position(0.53f, -0.92f, 0.0f, 0.0f)
		cards.getValue(13).position(1.59f, -0.92f, 0.0f, 0.0f)
		cards.getValue(14).position(2.65f, -0.92f, 0f, 0.0f)
		cards.getValue(15).position(3.71f, -0.92f, 0.0f, 0.0f)
	}
}

private fun setCompositionFor20Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-1.56f, 3.66f, 0.0f, 0.0f)
		cards.getValue(1).position(-0.52f, 3.66f, 0f, 0.0f)
		cards.getValue(2).position(0.52f, 3.66f, 0.0f, 0.0f)
		cards.getValue(3).position(1.56f, 3.66f, 0.0f, 0.0f)

		// второй ряд
		cards.getValue(4).position(-1.56f, 1.82f, 0f, 0.0f)
		cards.getValue(5).position(-0.52f, 1.82f, 0.0f, 0.0f)
		cards.getValue(6).position(0.52f, 1.82f, 0.0f, 0.0f)
		cards.getValue(7).position(1.56f, 1.82f, 0.0f, 0.0f)

		// третий ряд
		cards.getValue(8).position(-1.56f, -0.02f, 0.0f, 0.0f)
		cards.getValue(9).position(-0.52f, -0.02f, 0.0f, 0.0f)
		cards.getValue(10).position(0.52f, -0.02f, 0f, 0.0f)
		cards.getValue(11).position(1.56f, -0.02f, 0.0f, 0.0f)

		// четвертый ряд
		cards.getValue(12).position(-1.56f, -1.86f, 0.0f, 0.0f)
		cards.getValue(13).position(-0.52f, -1.86f, 0f, 0.0f)
		cards.getValue(14).position(0.52f, -1.86f, 0.0f, 0.0f)
		cards.getValue(15).position(1.56f, -1.86f, 0.0f, 0.0f)

		// пятый ряд
		cards.getValue(16).position(-1.56f, -3.7f, 0.0f, 0.0f)
		cards.getValue(17).position(-0.52f, -3.7f, 0f, 0.0f)
		cards.getValue(18).position(0.52f, -3.7f, 0.0f, 0.0f)
		cards.getValue(19).position(1.56f, -3.7f, 0.0f, 0.0f)
	} else {
		// первый ряд
		cards.getValue(0).position(-4.77f, 0.92f, 0.0f, 0.0f)
		cards.getValue(1).position(-3.71f, 0.92f, 0.0f, 0.0f)
		cards.getValue(2).position(-2.65f, 0.92f, 0.0f, 0.0f)
		cards.getValue(3).position(-1.59f, 0.92f, 0f, 0.0f)
		cards.getValue(4).position(-0.53f, 0.92f, 0.0f, 0.0f)
		cards.getValue(5).position(0.53f, 0.92f, 0.0f, 0.0f)
		cards.getValue(6).position(1.59f, 0.92f, 0f, 0.0f)
		cards.getValue(7).position(2.65f, 0.92f, 0.0f, 0.0f)
		cards.getValue(8).position(3.71f, 0.92f, 0f, 0.0f)
		cards.getValue(9).position(4.77f, 0.92f, 0f, 0.0f)

		// второй ряд
		cards.getValue(10).position(-4.77f, -0.92f, 0.0f, 0.0f)
		cards.getValue(11).position(-3.71f, -0.92f, 0.0f, 0.0f)
		cards.getValue(12).position(-2.65f, -0.92f, 0.0f, 0.0f)
		cards.getValue(13).position(-1.59f, -0.92f, 0f, 0.0f)
		cards.getValue(14).position(-0.53f, -0.92f, 0.0f, 0.0f)
		cards.getValue(15).position(0.53f, -0.92f, 0.0f, 0.0f)
		cards.getValue(16).position(1.59f, -0.92f, 0.0f, 0.0f)
		cards.getValue(17).position(2.65f, -0.92f, 0f, 0.0f)
		cards.getValue(18).position(3.71f, -0.92f, 0.0f, 0.0f)
		cards.getValue(19).position(4.77f, -0.92f, 0f, 0.0f)
	}
}

private fun setCompositionFor30Cards(cards: Map<Int, Card>, portrait: Boolean) {
	if (portrait) {
		// первый ряд
		cards.getValue(0).position(-2.08f, 4.6f, 0.0f, 0.0f)
		cards.getValue(1).position(-1.04f, 4.6f, 0f, 0.0f)
		cards.getValue(2).position(0f, 4.6f, 0.0f, 0.0f)
		cards.getValue(3).position(1.04f, 4.6f, 0.0f, 0.0f)
		cards.getValue(4).position(2.08f, 4.6f, 0f, 0.0f)

		// второй ряд
		cards.getValue(5).position(-2.08f, 2.76f, 0f, 0.0f)
		cards.getValue(6).position(-1.04f, 2.76f, 0.0f, 0.0f)
		cards.getValue(7).position(0f, 2.76f, 0.0f, 0.0f)
		cards.getValue(8).position(1.04f, 2.76f, 0.0f, 0.0f)
		cards.getValue(9).position(2.08f, 2.76f, 0.0f, 0.0f)

		// третий ряд
		cards.getValue(10).position(-2.08f, 0.92f, 0.0f, 0.0f)
		cards.getValue(11).position(-1.04f, 0.92f, 0.0f, 0.0f)
		cards.getValue(12).position(0f, 0.92f, 0f, 0.0f)
		cards.getValue(13).position(1.04f, 0.92f, 0.0f, 0.0f)
		cards.getValue(14).position(2.08f, 0.92f, 0.0f, 0.0f)

		// четвертый ряд
		cards.getValue(15).position(-2.08f, -0.92f, 0.0f, 0.0f)
		cards.getValue(16).position(-1.04f, -0.92f, 0f, 0.0f)
		cards.getValue(17).position(0f, -0.92f, 0.0f, 0.0f)
		cards.getValue(18).position(1.04f, -0.92f, 0.0f, 0.0f)
		cards.getValue(19).position(2.08f, -0.92f, 0.0f, 0.0f)

		// пятый ряд
		cards.getValue(20).position(-2.08f, -2.76f, 0.0f, 0.0f)
		cards.getValue(21).position(-1.04f, -2.76f, 0f, 0.0f)
		cards.getValue(22).position(0f, -2.76f, 0.0f, 0.0f)
		cards.getValue(23).position(1.04f, -2.76f, 0.0f, 0.0f)
		cards.getValue(24).position(2.08f, -2.76f, 0.0f, 0.0f)

		// шестой ряд
		cards.getValue(25).position(-2.08f, -4.6f, 0.0f, 0.0f)
		cards.getValue(26).position(-1.04f, -4.6f, 0f, 0.0f)
		cards.getValue(27).position(0f, -4.6f, 0.0f, 0.0f)
		cards.getValue(28).position(1.04f, -4.6f, 0.0f, 0.0f)
		cards.getValue(29).position(2.08f, -4.6f, 0.0f, 0.0f)
	} else {
		// первый ряд
		cards.getValue(0).position(-4.77f, 1.84f, 0.0f, 0.0f)
		cards.getValue(1).position(-3.71f, 1.84f, 0.0f, 0.0f)
		cards.getValue(2).position(-2.65f, 1.84f, 0.0f, 0.0f)
		cards.getValue(3).position(-1.59f, 1.84f, 0f, 0.0f)
		cards.getValue(4).position(-0.53f, 1.84f, 0.0f, 0.0f)
		cards.getValue(5).position(0.53f, 1.84f, 0.0f, 0.0f)
		cards.getValue(6).position(1.59f, 1.84f, 0f, 0.0f)
		cards.getValue(7).position(2.65f, 1.84f, 0.0f, 0.0f)
		cards.getValue(8).position(3.71f, 1.84f, 0f, 0.0f)
		cards.getValue(9).position(4.77f, 1.84f, 0f, 0.0f)

		// второй ряд
		cards.getValue(10).position(-4.77f, 0f, 0.0f, 0.0f)
		cards.getValue(11).position(-3.71f, 0f, 0.0f, 0.0f)
		cards.getValue(12).position(-2.65f, 0f, 0.0f, 0.0f)
		cards.getValue(13).position(-1.59f, 0f, 0f, 0.0f)
		cards.getValue(14).position(-0.53f, 0f, 0.0f, 0.0f)
		cards.getValue(15).position(0.53f, 0f, 0.0f, 0.0f)
		cards.getValue(16).position(1.59f, 0f, 0.0f, 0.0f)
		cards.getValue(17).position(2.65f, 0f, 0f, 0.0f)
		cards.getValue(18).position(3.71f, 0f, 0.0f, 0.0f)
		cards.getValue(19).position(4.77f, 0f, 0f, 0.0f)

		// третий ряд
		cards.getValue(20).position(-4.77f, -1.84f, 0.0f, 0.0f)
		cards.getValue(21).position(-3.71f, -1.84f, 0.0f, 0.0f)
		cards.getValue(22).position(-2.65f, -1.84f, 0.0f, 0.0f)
		cards.getValue(23).position(-1.59f, -1.84f, 0f, 0.0f)
		cards.getValue(24).position(-0.53f, -1.84f, 0.0f, 0.0f)
		cards.getValue(25).position(0.53f, -1.84f, 0.0f, 0.0f)
		cards.getValue(26).position(1.59f, -1.84f, 0.0f, 0.0f)
		cards.getValue(27).position(2.65f, -1.84f, 0f, 0.0f)
		cards.getValue(28).position(3.71f, -1.84f, 0.0f, 0.0f)
		cards.getValue(29).position(4.77f, -1.84f, 0f, 0.0f)
	}
}
