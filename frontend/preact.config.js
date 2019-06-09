const UglifyJsPlugin = require('uglifyjs-webpack-plugin');

export default (config, env, helpers) => {
    console.log("getPlugin");
    const uglufiyJsPlugin = helpers.getPluginsByName(config, 'UglifyJsPlugin')[0];

    if(uglufiyJsPlugin){
        console.log("getIndex");
        const {index} = uglufiyJsPlugin;

        console.log("Splice");
        config.plugins.splice(index, 1);

        console.log("add");
        //config.plugins.push(new UglifyJsPlugin());
    }
};