import {useCallback, useState} from "react";

const SandBox = ({precode = ""}) => {

    const [code, setCode] = useState<string>(precode);
    const [result, setResult] = useState<string>("");
    const [error, setError] = useState<string | null>(null);
    const [isLoading, setIsLoading] = useState<boolean>(false);

    const execute = useCallback(async () => {
        setIsLoading(true);
        const promise = await fetch("https://abderrahmenlamloumi.github.io/Deca-Compiler-Java/compile", {
            method: "POST",
            body: code,
        })
        if (promise.status === 200) {
            const data = await promise.json()
            setResult(data.ima)
            setError(data.decac)
        } else {
            setResult("Error occurred while fetching code");
        }
        setIsLoading(false);

    }, [code])


    return (
        <div className="sandbox" style={{
            display: 'flex',
            flexDirection: 'column',
            width: "100%",
            minHeight: "300px",
            gap: "1em"
        }}>
            <div style={{
                boxShadow: "0px 0px 8px #00000080",

            }}>
                <div style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    cursor: 'pointer',

                }}>
                    <div style={{
                        borderRight: "1px solid #00000080",
                        padding: "1em",
                    }}
                    onClick={execute}
                    >Executer</div>
                    <div style={{opacity: isLoading ? 1 : 0}}>Chargement ...</div>
                    <div style={{flex: 1}}></div>
                </div>
            </div>
            <div style={{
                display: 'flex',
                width: "100%",
                flex: 1,
                gap: "1em",


            }}>
                <textarea value={code} onChange={e => setCode(e.target.value)}
                     style={{
                         flex: 1,
                         width: "100%",
                         padding: "20px",
                         boxShadow: "0px 0px 8px #00000080 inset",
                         border: "none",
                     }} placeholder="Ecris du code ici..."
                >

                </textarea>
                <div
                     style={{
                         flex: 1,
                         width: "100%",
                         padding: "20px",
                         boxShadow: "0px 0px 8px #00000080 inset",
                         whiteSpace: "pre-wrap"
                     }}
                >
                    {result}
                    <div style={{
                        fontSize: "10px",
                        padding: "10px",
                        boxSizing: "border-box",
                        backgroundColor: "#FF8888",
                        opacity: (error !== null && error !== "") ? 1 : 0,
                    }}>
                        {error}
                    </div>

                </div>
            </div>

        </div>
    );
};

export default SandBox;