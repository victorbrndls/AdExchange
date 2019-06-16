const TextAds = [
    {
        text: "Anúncio de texto, voce pode alterar **o estilo** do texto nos __campos abaixo__.",
        parsedOutput: [
            {
                tag: 'span',
                content: 'Anúncio de texto, voce pode alterar '
            }, {
                tag: 'b',
                content: 'o estilo '
            }, {
                tag: 'span',
                content: 'do texto nos '
            }, {
                tag: 'i',
                content: 'campos abaixo.'
            }
        ]
        ,
        bgColor: "#ffffff",
        textColor: "#000000",
        textAlignment: "LEFT",
        textSize: 16,
    },
    {
        text: "**Aumente a divulgação de seus produtos** anunciando em nosso site. Clique aqui para saber como.",
        parsedOutput: [
            {
                "tag": "b",
                "content": "Aumente a divulgação de seus produtos"
            },
            {
                "tag": "span",
                "content": " anunciando em nosso site. Clique aqui para saber como."
            }],
        bgColor: "#ffffff",
        textColor: "#000000",
        textAlignment: "CENTER",
        textSize: 18,
    }
];

const ImageAds = [
    {
        imageUrl: "https://i.imgur.com/SQFA7oF.jpg"
    }
];


export default {
    TextAds,
    ImageAds
}