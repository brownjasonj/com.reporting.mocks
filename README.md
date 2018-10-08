<h1>MockRiskGenerator</h1>
This is a very simplistic simulation of events that one might expect from a trading risk system.  There are
three parts to the simulator:

1) Activity simulation - this part simulates the different activities that would trigger risks to be calculated.
the activities are:  market data changes, trading activity and business event activity (e.g., end of day)

2) Business process simulation - this simulates the processes that are undertaken as a consequence of the 
above activities.  This includes calculating the valuations and risks for trading activity and business events.

3) Persistence and publication - this part simulates the data storage, indexing and publishing of the 
consequences of the activity and business processes.

<h1>Additional Packages</h1>
Before you can build the project you need to fork the additional github projects:
<p> 
    <ul>
    <li>https://github.com/brownjasonj/com.reporting.mocks.model</li>
    <li>https://github.com/brownjasonj/com.reporting.kafka.serialization</li>
    </ul>
</p>
This is a set of classes that model the set of business objects such as risks, trades and markets and their associated kafka serilaizers.


<h1>Using the simulator</h1>
The simulator automatically publishes to Kafka, so you will need to install both Zookeeper and Kafka and have those
running.  Take a look at the application.yml file to understand which ports and topics are defined, these can be
changed to whatever you want.

<pre>
spring:
  profiles: default
server:
  port: 30001
  #port: ${PORT:${SERVER_PORT:0}}
version: 0.0.1
kafka:
  server: localhost:9092
  topic:
    intradayriskset: IntraDayRiskSet
    intradayrisktick: IntraDayRiskTick
    calccontext: CalculationContext
    market: Market
</pre>

To start the simulator run the following

gradle bootRun

Use the swagger ui to see all available endpoints (<a href="http://localhost:30001/swagger-ui.html">http://localhost:30001/swagger-ui.html</a>)

The behaviour of the emulator is defined by a configuration. There is one predefined configuration, but it is possible
to create an setup new configurations.  To see all configurations available got to
<a href="http://localhost:30001/getpricinggroups">http://localhost:30001/getpricinggroups</a>.

To start the emulator you need to got to the process start endpoint and start it for a specific configuration.  As 
seen above the predefined configuration is 'fxdesk'.  Start the emulator using
<a href="http://localhost:30001/controlprocess/start/fxdesk">http://localhost:30001/controlprocess/start/fxdesk</a>.
This will start all the emulator processes and you should see some output to the console, something like:
<pre>
TradeEvent New
Trade Event New trade: {Type: Forward, Book: book:fxdesk:fxspots:Book3, TCN: 883967d8-c974-41c4-9272-79c92e9692f0.0}
TradeEvent New
Trade Event New trade: {Type: Forward, Book: book:fxdesk:fxspots:Book2, TCN: 263a1b9d-3944-46aa-95f5-dc67261e68e2.0}
TradeEvent New
Trade Event New trade: {Type: Forward, Book: book:fxdesk:fxspots:Book2, TCN: eb76ae06-bd31-4f69-aa1b-2caa164307c8.0}
</pre>


<h2> Structure of the code </h2>
There are six packages

<h3>com.reporting.mocks.configuration</h3>
This contains class models to hold the application configuration and is broken down into
three main parts
<pre>
{
      "pricingGroupId": {
        "name": "fxdesk"
      },
      "tradeConfig": {
        "startingTradeCount": 1000,
        "newTradeStart": 0,
        "newTradePeriodicity": 1000,
        "modifiedTradeStart": 60000,
        "modifiedTradePeriodicity": 60000,
        "deleteTadeStart": 120000,
        "deleteTradePeriodicity": 120000,
        "books": [
          "book:fxdesk:fxspots:Book1",
          "book:fxdesk:fxspots:Book2",
          "book:fxdesk:fxspots:Book3"
        ],
        "otcUnderlying": [
          {
            "ccy1": "EUR",
            "ccy2": "USD",
            "accy": "USD",
            "componenetCount": 2,
            "underlyingCurrency": {
              "currency": "USD"
            }
          },
          {
            "ccy1": "EUR",
            "ccy2": "CHF",
            "accy": "CHF",
            "componenetCount": 2,
            "underlyingCurrency": {
              "currency": "CHF"
            }
          },
        ...
        ],
        "tradeTypes": [
          "Spot",
          "Forward",
          "Swap"
        ],
        "securityStatic": []
      },
      "intradayConfig": {
        "risks": [
          {
            "riskType": "PV",
            "periodicity": 1
          },
          {
            "riskType": "DELTA",
            "periodicity": 3
          },
          {
            "riskType": "VEGA",
            "periodicity": 3
          }
        ]
      },
      "endofdayConfig": {
        "risks": [
          "PV",
          "DELTA",
          "VEGA"
        ],
        "periodicity": 600000
      },
      "eod": true,
      "sod": true,
      "ind": true,
      "marketPeriodicity": 600000
    }
</pre>
<h4>TradeConfig</h4>
Defines the trade types, underlyings and the rate at which new, modify and delete trade events occur.

<h4>IntradayConfig</h4>
Defines the periodicity at which intraday market events occur.  These market events trigger subsequent risk calculations.

<h4>EndofDayConfig</h4>
Defines the periodicity at which end of day market events occur.

<h3>com.reporting.mocks.Controllers</h3>
This package contains all the REST endpoints of the application.  Different urls are provided to start
the application processing, provide access to trade populations and market data.

<h3>com.reporting.mocks.endpoints</h3>
This package contains a set of example publishers of the events generated by the application.  Three are
provided for publishing to Java Queues, Apache Kafka and to Apache Ignite.

<h3>com.reporting.mocks.generators</h3>
This package contains the a set of classes to generate different events and risk results.

<h3>com.reporting.mocks.peristence</h3>
This is a simple mock persitence layer.

<h3>com.reporting.mocks.process</h3>
The main set of classes that create the threads for generating all the events, be that market or trade events.

<h1>Description of the Emulator</h1>
There are six main business objects 

<ul>
<li><b>Trade</b> a trade consisting of a trade type (e.g., Spot, Forward, Swap)
    <pre>
{
    "kind": "Otc",
    "tcn": {
      "id": "45362f65-c63a-4bf8-9618-2b056efa547f",
      "version": 0
    },
    "book": "book:fxdesk:fxspots:Book1",
    "tradeType": "Forward",
    "quantity": null,
    "buySell": null,
    "underlying": {
      "ccy1": "USD",
      "ccy2": "MXN",
      "underlyingCurrency": {
        "currency": "MXN"
      },
      "componenetCount": 2,
      "accy": "MXN"
    },
    "version": 0
  }
        </pre>
    </li>
<li><b>TradePopulation</b> a set of trades plus a label (e.g., EOD, Intraday).
    <pre>
    [
      {
        "kind": "Otc",
        "tcn": {
          "id": "536e07e5-bf88-469d-b462-3d4d891143ac",
          "version": 0
        },
        "book": "book:fxdesk:fxspots:Book2",
        "tradeType": "Forward",
        "quantity": null,
        "buySell": null,
        "underlying": {
          "ccy1": "EUR",
          "ccy2": "MXN",
          "accy": "MXN",
          "componenetCount": 2,
          "underlyingCurrency": {
            "currency": "MXN"
          }
        },
        "version": 0
      },
      ...
    ]
    </pre> 
</li>
<li><b>Risk</b> has a type (e.g., PV, Delta, Gamma) and value which the simulator assigns a random value.
<pre>
{
                "nameValue":"value",
                "nameCurrency":"currency",
                "calculationContextId":{
                    "pricingGroupName":"fxdesk",
                    "locator":"/calculationcontext/fxdesk",
                    "uri":"/calculationcontext/fxdesk/2f3e4641-3a73-46d5-8ab3-3073afbc6a34",
                    "id":"2f3e4641-3a73-46d5-8ab3-3073afbc6a34"
                },
                "marketEnvId":{
                    "locator":"/calculationcontext/market/fxdesk",
                    "uri":"/calculationcontext/market/fxdesk/287e3617-e015-4382-9e75-07c418a51c1e",
                    "id":"287e3617-e015-4382-9e75-07c418a51c1e"
                },
                "tradePopulationId":{
                    "locator":"/tradepopulation/fxdesk",
                    "uri":"/tradepopulation/fxdesk/7f745b6d-6ffd-4a02-a6a4-7024fa7f3f96",
                    "id":"7f745b6d-6ffd-4a02-a6a4-7024fa7f3f96"
                },
                "riskRunId":{
                    "locator":"/riskrun/fxdesk",
                    "uri":"/riskrun/fxdesk/262ba5b2-2357-4dee-a72f-33e1d56debaf",
                    "id":"262ba5b2-2357-4dee-a72f-33e1d56debaf"
                },
                "bookName":"book:fxdesk:fxspots:Book1",
                "tcn":{
                    "id":"5d19fb54-b414-48cf-85d6-0ef5898eff26",
                    "version":0
                },
                "riskType":"PV",
                "kvp":{
                    "currency":{"currency":"GBP"},
                    "value":0.0077574439267200646
                }
            }
</pre>
</li>
<li><b>CalculationContext</b> a set of <b>Market</b> objects and a map from <b>RiskType</b> (e.g., PV, Delta, Gamma) to one of the <b>Market</b> objects in the set.
<pre>
{
    "id": {
       "locator": "/calculationcontext/fxdesk",
       "uri": "/calculationcontext/fxdesk/3014a121-37f8-4f99-8c86-dfc66b2fb973",
       "id": "3014a121-37f8-4f99-8c86-dfc66b2fb973",
       "pricingGroupName": "fxdesk"
    },
    "timeStamp": "2018-10-08T11:48:16.690+0000",
    "markets": {
      "PV": {
        "locator": "/calculationcontext/market/fxdesk",
        "uri": "/calculationcontext/market/fxdesk/4ed89b3c-c40a-444a-9e9a-46464d091047",
        "id": "4ed89b3c-c40a-444a-9e9a-46464d091047"
      },
      "DELTA": {
        "locator": "/calculationcontext/market/fxdesk",
        "uri": "/calculationcontext/market/fxdesk/4ed89b3c-c40a-444a-9e9a-46464d091047",
        "id": "4ed89b3c-c40a-444a-9e9a-46464d091047"
      },
      "VEGA": {
        "locator": "/calculationcontext/market/fxdesk",
        "uri": "/calculationcontext/market/fxdesk/4ed89b3c-c40a-444a-9e9a-46464d091047",
        "id": "4ed89b3c-c40a-444a-9e9a-46464d091047"
      }
    }
  }
</pre>
In the above example the market context is for the pricing group "fxdesk".  There are three markets, one for each
of the risk types PV, DELTA and VEGA.  In the above example all three markets have the same id and are therefore 
refer to the same market.

Later there is a market change and a new context is created as below:
<pre>
{
    "id": {
      "locator": "/calculationcontext/fxdesk",
      "uri": "/calculationcontext/fxdesk/c3c09478-31df-4506-beba-8d650f5edc59",
      "id": "c3c09478-31df-4506-beba-8d650f5edc59",
      "pricingGroupName": "fxdesk"
    },
    "timeStamp": "2018-10-08T11:48:16.695+0000",
    "markets": {
      "PV": {
        "locator": "/calculationcontext/market/fxdesk",
        "uri": "/calculationcontext/market/fxdesk/8ebcd97a-f518-45f3-a6ff-a330dda87e8c",
        "id": "8ebcd97a-f518-45f3-a6ff-a330dda87e8c"
      },
      "DELTA": {
        "locator": "/calculationcontext/market/fxdesk",
        "uri": "/calculationcontext/market/fxdesk/ebb36bd3-9b33-4c3c-871a-3c74e5677883",
        "id": "ebb36bd3-9b33-4c3c-871a-3c74e5677883"
      },
      "VEGA": {
        "locator": "/calculationcontext/market/fxdesk",
        "uri": "/calculationcontext/market/fxdesk/ebb36bd3-9b33-4c3c-871a-3c74e5677883",
        "id": "ebb36bd3-9b33-4c3c-871a-3c74e5677883"
      }
    }
  }
</pre>
Note that only the market for PV has changed.  If you look at the configuration above you will see in the section
<pre>
"intradayConfig": {
    "risks": [
      {
        "riskType": "PV",
        "periodicity": 1
      },
      {
        "riskType": "DELTA",
        "periodicity": 3
      },
      {
        "riskType": "VEGA",
        "periodicity": 3
      }
    ]
  }
</pre>
The "periodicity" value determines when the markets for each of the risks should be updated.  In this case the PV market
should be updated whenever the market changes, Delta and Vega markets are updated when there are 3 market changes.
</li>
<li><b>Market</b> represents market data.  No actual market data is represented, this object has an id and timestamp (the time the market was notionally created).  All risk values created by the simulator have an associated <b>Market</b> to represent the fact that the risk was calculated in the context of that market
<pre>
{
  "id": {
     "locator": "/calculationcontext/market/fxdesk",
     "uri": "/calculationcontext/market/fxdesk/4ed89b3c-c40a-444a-9e9a-46464d091047",
     "id": "4ed89b3c-c40a-444a-9e9a-46464d091047"
  },
  "asOf": "2018-10-08T11:48:16.690+0000",
  "type": "EOD"
}
</pre>
</li>

<li><b>RiskResult</b> consists of a set of <b>Risk</b> values, a reference to a <b>CalculationContext</b>, a <b>TradePopulation</b>, a <b>RiskRunId</b> signifying which risk run the result pertains to, a <b>fragment count</b> and <b>fragment number</b>.  If there are a large number of risk results for a risk run then a set of </b>RiskResult</b> objects will be generated each with the same <b>RiskRunId</b> but different fragment numbers. </li>
<pre>
{
    "calculationContextId":{
        "pricingGroupName":"fxdesk",
        "locator":"/calculationcontext/fxdesk",
        "uri":"/calculationcontext/fxdesk/2f3e4641-3a73-46d5-8ab3-3073afbc6a34",
        "id":"2f3e4641-3a73-46d5-8ab3-3073afbc6a34"
        },
    "tradePopulationId":{
        "locator":"/tradepopulation/fxdesk",
        "uri":"/tradepopulation/fxdesk/7f745b6d-6ffd-4a02-a6a4-7024fa7f3f96",
        "id":"7f745b6d-6ffd-4a02-a6a4-7024fa7f3f96"
    },
    "riskRunId":{
        "locator":"/RiskRun/fxdesk",
        "uri":"/RiskRun/fxdesk/8e8eb591-cf61-43b7-8434-cfc79d8dddc0",
        "id":"8e8eb591-cf61-43b7-8434-cfc79d8dddc0"},
        "fragmentCount":3,
        "fragmentNo":0,
        "results":[
            {
                "nameValue":"value",
                "nameCurrency":"currency",
                "calculationContextId":{
                    "pricingGroupName":"fxdesk",
                    "locator":"/calculationcontext/fxdesk",
                    "uri":"/calculationcontext/fxdesk/2f3e4641-3a73-46d5-8ab3-3073afbc6a34",
                    "id":"2f3e4641-3a73-46d5-8ab3-3073afbc6a34"
                },
                "marketEnvId":{
                    "locator":"/calculationcontext/market/fxdesk",
                    "uri":"/calculationcontext/market/fxdesk/287e3617-e015-4382-9e75-07c418a51c1e",
                    "id":"287e3617-e015-4382-9e75-07c418a51c1e"
                },
                "tradePopulationId":{
                    "locator":"/tradepopulation/fxdesk",
                    "uri":"/tradepopulation/fxdesk/7f745b6d-6ffd-4a02-a6a4-7024fa7f3f96",
                    "id":"7f745b6d-6ffd-4a02-a6a4-7024fa7f3f96"
                },
                "riskRunId":{
                    "locator":"/riskrun/fxdesk",
                    "uri":"/riskrun/fxdesk/262ba5b2-2357-4dee-a72f-33e1d56debaf",
                    "id":"262ba5b2-2357-4dee-a72f-33e1d56debaf"
                },
                "bookName":"book:fxdesk:fxspots:Book1",
                "tcn":{
                    "id":"5d19fb54-b414-48cf-85d6-0ef5898eff26",
                    "version":0
                },
                "riskType":"PV",
                "kvp":{
                    "currency":{"currency":"GBP"},
                    "value":0.0077574439267200646
                }
            }],
        "isDeleteEvent":false}
}
</pre>
</ul>

The simulator simulates the following set of events

<ul>
<li><b>Trade Event</b> - at random intervals the simulator will create a <b>New</b> trade , <b>Modify</b> a trade in the <b>TradePopulation</b> or <b>Delete</b> a trade from the </b>TradePopulation</b>.  This trade event will trigger one or more <b>Risk</b> values to be generated and thus one or more <b>RiskResult</b> objects to be generated.</li>
<li><b>Market Event</b> - a market event is a configurably timed trigger that generates a new <b>Market</b> object.</li>
<li><b>End-of-Day Event</b></li>
<ul>