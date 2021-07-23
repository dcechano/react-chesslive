import {useRef, useState} from "react";

const Login = props => {

    const usernameRef = useRef();
    const passwordRef = useRef();
    const [jwt, setJwt] = useState(null);

    const onSubmitHandler = event => {
        event.preventDefault();
        login();
    };

    const login = async () => {
        try {
            const response = await fetch("http://localhost:8080/api/authenticate", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },

                body: JSON.stringify({
                    username: 'jane',
                    password: 'password'
                })
            });
            if (!response.ok) {
            // Handle error on UI
            }

            const body = await response.json();

            console.log(`First response body: ${JSON.stringify(body)}`);
            console.log(body.jwt)
            console.log(JSON.stringify(body.jwt));
            console.log(typeof body.jwt);
            setJwt(body.jwt);

            const response2 = await fetch("http://localhost:8080/api/game/gameID", {
                method: 'GET',
                headers: {
                    Accept: 'application/json',
                    Authorization: `Bearer ${body.jwt}`
                }
            });

            console.log(response);
            if (!response2.ok) {
                console.log("Second request failed");
            }

            const body2 = await response2.json();

            console.log(`Second response body: ${JSON.stringify(body2)}`);

        } catch (e) {
            console.log(e);
        }
    };

    return (
        <form onSubmit={onSubmitHandler}>
            <div>
                <label htmlFor="username">Username</label>
                <input ref={usernameRef} id='username' type="text"/>
            </div>
            <div>
                <label htmlFor="password">Password</label>
                <input ref={passwordRef} id='password' type="text"/>
            </div>
            <button type='submit'>Submit</button>
        </form>
    );
};

export default Login;