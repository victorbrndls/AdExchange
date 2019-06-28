const CPF_PATTERN = "XXX.XXX.XXX-XX";

/*
*
* @param cpf {String}
* @return the formatted CPF
* */
function formatCpf(cpf) {
    return formatNumericPattern(CPF_PATTERN, cpf);
}

function formatNumericPattern(pattern, text) {
    let numbers = text.replace(/\D/g, ""); // Remove non digits

    let formattedText = pattern;

    for (let num of numbers) {
        formattedText = formattedText.replace(/X/, num); // Replace the 'X' in the pattern with the actual number
    }

    return formattedText.replace(/X/g, '') // Remove the remaining 'X's
        .replace(/\D+$/, ''); // Remove the remaining non digits from the end
}

export default {
    formatCpf
}

