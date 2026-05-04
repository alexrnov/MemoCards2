package alexrnov.animememo.view.binding

import androidx.databinding.ObservableField

class ExitDialogData() {
	val title = ObservableField<String>()
	val dialogText = ObservableField<String>()

	fun update(title: String, dialogText: String) {
		this.title.set(title)
		this.dialogText.set(dialogText)
	}
}