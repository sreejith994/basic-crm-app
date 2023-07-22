import {Button, ButtonGroup, Spinner, Text, Wrap, WrapItem} from '@chakra-ui/react';
import SidebarWithHeader from "./components/shared/sidebar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import CardWithImage from "./components/shared/Card.jsx";

const App = () => {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        getCustomers().then(res => {
            setCustomers(res)
        }).catch(error => console.log(error))
            .finally(() => setLoading(false))
    }, []);

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>)
    }

    if (customers.length <= 0) {
        return (
            <SidebarWithHeader>
                <Text>No Data Available
                </Text>
            </SidebarWithHeader>
        )
    }

    return (

        <SidebarWithHeader>
            <Wrap spacing={"30px"}>
                {customers.map((customer, index) => (
                    <WrapItem key={index} >
                        <CardWithImage
                            key={index}
                            {... customer}

                        />
                    </WrapItem>
                    )
                )
                };
            </Wrap>


        </SidebarWithHeader>
    )
};

export default App;

