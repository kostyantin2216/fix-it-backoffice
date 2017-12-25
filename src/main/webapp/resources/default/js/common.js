function displayErrorMessage(data) {
    document.getElementById("error-messages").innerText =
        "Error Description: " + data.description
        + "\nError Name: " + data.errorName
        + "\nError errorMessage: " + data.errorMessage
        + "\nResponse Code: " + data.responseCode
        + "\nResponse Text: " + data.responseText
        + "\nStatus: " + data.status
        + "\nType: " + data.type;
}