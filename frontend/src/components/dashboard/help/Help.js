import {Component} from "preact";

export default class Help extends Component {
    constructor(props) {
        super(props);
    }

    render() {
        return (
            <div class="container">
                <div class="row mb-3">
                    <div class="col">
                        <div class="help-header">
                            Suporte
                        </div>
                    </div>
                </div>

                {/* Website owner roadmap*/}

                <div class="row">
                    <div class="col">
                        <dl class="help-item--container">
                            <dt>Dashboard</dt>
                            <dd>
                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. At consequatur debitis
                                doloribus exercitationem illo ipsum itaque iure magni obcaecati perspiciatis quam
                                quod repudiandae sed sint tempore unde, vero voluptate. Fugiat? Lorem ipsum dolor sit
                                amet, consectetur adipisicing elit. At consequatur debitis doloribus exercitationem illo
                                ipsum itaque iure magni obcaecati perspiciatis quam quod repudiandae sed sint tempore
                                unde, vero voluptate. Fugiat?
                            </dd>
                        </dl>

                        <dl class="help-item--container">
                            <dt>Websites</dt>
                            <dd>
                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. At consequatur debitis
                                doloribus exercitationem illo ipsum itaque iure magni obcaecati perspiciatis quam
                                quod repudiandae sed sint tempore unde, vero voluptate. Fugiat? Lorem ipsum dolor sit
                                amet, consectetur adipisicing elit. At consequatur debitis doloribus exercitationem illo
                                ipsum itaque iure magni obcaecati perspiciatis quam quod repudiandae sed sint tempore
                                unde, vero voluptate. Fugiat?
                            </dd>
                        </dl>

                        <dl class="help-item--container">
                            <dt>Propostas</dt>
                            <dd>
                                Lorem ipsum dolor sit amet, consectetur adipisicing elit. At consequatur debitis
                                doloribus exercitationem illo ipsum itaque iure magni obcaecati perspiciatis quam
                                quod repudiandae sed sint tempore unde, vero voluptate. Fugiat? Lorem ipsum dolor sit
                                amet, consectetur adipisicing elit. At consequatur debitis doloribus exercitationem illo
                                ipsum itaque iure magni obcaecati perspiciatis quam quod repudiandae sed sint tempore
                                unde, vero voluptate. Fugiat?
                            </dd>
                        </dl>
                    </div>
                </div>
            </div>
        )
    }
}