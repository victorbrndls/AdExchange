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
        text: "**Aumente a divulgação de seus produtos** anunciando em nosso site. \\\\ \\\\Clique aqui para saber como.",
        parsedOutput: [
            {
                "tag": "b",
                "content": "Aumente a divulgação de seus produtos"
            },
            {
                "tag": "span",
                "content": " anunciando em nosso site. "
            }, {
                "tag": "br",
                "content": ""
            }, {
                "tag": "span"
            }, {
                "tag": "br"
            }, {
                "tag": "span",
                "content": "Clique aqui para saber como."
            }],
        bgColor: "#ffffff",
        textColor: "#000000",
        textAlignment: "CENTER",
        textSize: 18,
    },
    {
        text: "Anúncio livre! \\\\ \\\\**Divulgue seu produto aqui!!!**",
        parsedOutput: [
            {
                "tag": "span",
                "content": "Anúncio livre! "
            }, {
                "tag": "br",
                "content": ""
            }, {
                "tag": "br"
            }, {
                "tag": "b",
                "content": "Divulgue seu produto aqui!!!"
            }],
        bgColor: "#0080ff",
        textColor: "#ffffff",
        textAlignment: "CENTER",
        textSize: 18,
    }
];

const ImageAds = [
    {
        imageUrl: "https://i.imgur.com/SQFA7oF.jpg"
    },
    {
        imageUrl: "https://i.imgur.com/3L4dJX7.jpg"
    }
];


export default {
    TextAds,
    ImageAds
}