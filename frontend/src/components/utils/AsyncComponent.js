import {Component} from "preact";

// https://scotch.io/tutorials/lazy-loading-routes-in-react
export default function asyncComponent(getComponent) {
    class AsyncComponent extends Component {
        constructor(props) {
            super(props);

            this.state = {
                Component: null
            };
        }

        componentWillMount() {
            if (!this.state.Component) {
                getComponent().then(Component => {
                    AsyncComponent.Component = Component;
                    this.setState({Component})
                })
            }
        }

        render() {
            const {Component} = this.state;

            if (Component)
                return (<Component {...this.props}/>);

            return null;
        }
    }

    return AsyncComponent;
}