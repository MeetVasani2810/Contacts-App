import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.roomdatabase.ContactEvent
import com.example.roomdatabase.ContactState

@Composable
fun AddContactDialog(
    state: ContactState,
    onEvent: (ContactEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier.padding(16.dp),
        onDismissRequest = { onEvent(ContactEvent.HideDialog) },
        title = {
            Text(
                text = "Add Contact",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = { onEvent(ContactEvent.SetFirstName(it)) },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = { onEvent(ContactEvent.SetLastName(it)) },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.phoneNumber,
                    onValueChange = { onEvent(ContactEvent.SetPhoneNumber(it)) },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onEvent(ContactEvent.SavedContact) },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = { onEvent(ContactEvent.HideDialog) }) {
                Text("Cancel")
            }
        }
    )
}
